<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="院系代码" prop="departmentCode">
        <el-input
          v-model="queryParams.departmentCode"
          placeholder="请输入院系代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="院系名称" prop="departmentName">
        <el-input
          v-model="queryParams.departmentName"
          placeholder="请输入院系名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="系主任" prop="headFacultyName">
        <el-select
          v-model="queryParams.headFacultyId"
          placeholder="请输入系主任姓名搜索"
          clearable
          filterable
          remote
          :remote-method="remoteSearchHeadFaculty"
          @change="handleQuery"
          style="width: 200px"
        >
          <el-option
            v-for="item in headFacultyOptions"
            :key="item.facultyId"
            :label="item.name"
            :value="item.facultyId"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['Dep:department:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['Dep:department:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['Dep:department:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['Dep:department:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="departmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="departmentId" />
      <el-table-column label="院系代码" align="center" prop="departmentCode" />
      <el-table-column label="院系名称" align="center" prop="departmentName" />
      <el-table-column label="系主任" align="center" prop="headFacultyName" />
      <el-table-column label="简介" align="center" prop="description" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['Dep:department:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['Dep:department:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改院系信息管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="院系代码" prop="departmentCode">
          <el-input v-model="form.departmentCode" placeholder="请输入院系代码" />
        </el-form-item>
        <el-form-item label="院系名称" prop="departmentName">
          <el-input v-model="form.departmentName" placeholder="请输入院系名称" />
        </el-form-item>
        <el-form-item label="系主任" prop="headFacultyId">
          <el-select
            v-model="form.headFacultyId"
            placeholder="请选择系主任"
            clearable
            filterable
            remote
            :remote-method="remoteSearchHeadFaculty"
            style="width: 100%"
          >
            <el-option
              v-for="item in headFacultyOptions"
              :key="item.facultyId"
              :label="item.name + ' (' + item.facultyNumber + ')'"
              :value="item.facultyId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDepartment, getDepartment, delDepartment, addDepartment, updateDepartment, searchFaculty } from "@/api/Dep/department"

export default {
  name: "Department",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 院系信息管理表格数据
      departmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 系主任搜索选项
      headFacultyOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        departmentCode: null,
        departmentName: null,
        headFacultyId: null,
        headFacultyName: null,
        description: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        departmentCode: [
          { required: true, message: "院系代码不能为空", trigger: "blur" },
          { pattern: /^[A-Za-z0-9]{6}$/, message: "院系代码必须为6位字母或数字", trigger: "blur" }
        ],
        departmentName: [
          { required: true, message: "院系名称不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询院系信息管理列表 */
    getList() {
      this.loading = true
      listDepartment(this.queryParams).then(response => {
        this.departmentList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        departmentId: null,
        departmentCode: null,
        departmentName: null,
        headFacultyId: null,
        headFacultyName: null,
        description: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.departmentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加院系信息管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const departmentId = row.departmentId || this.ids
      getDepartment(departmentId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改院系信息管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.departmentId != null) {
            updateDepartment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDepartment(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const departmentIds = row.departmentId || this.ids
      this.$modal.confirm('是否确认删除院系信息管理编号为"' + departmentIds + '"的数据项？').then(function() {
        return delDepartment(departmentIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('Dep/department/export', {
        ...this.queryParams
      }, `department_${new Date().getTime()}.xlsx`)
    },
    /** 远程搜索系主任 */
    remoteSearchHeadFaculty(query) {
      if (query.length < 1) {
        this.headFacultyOptions = []
        return
      }
      searchFaculty({ name: query }).then(response => {
        this.headFacultyOptions = response.rows || []
      })
    }
  }
}
</script>
