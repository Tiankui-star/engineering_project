<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工号" prop="facultyNumber">
        <el-input
          v-model="queryParams.facultyNumber"
          placeholder="请输入工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="职称" prop="title">
        <el-select v-model="queryParams.title" placeholder="请选择职称" clearable>
          <el-option
            v-for="dict in dict.type.usr_prof"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="研究领域" prop="researchField">
        <el-input
          v-model="queryParams.researchField"
          placeholder="请输入研究领域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属院系" prop="departmentId">
        <el-select
          v-model="queryParams.departmentId"
          placeholder="请选择所属院系"
          clearable
          filterable
          @change="handleQuery"
        >
          <el-option
            v-for="department in departmentList"
            :key="department.departmentId"
            :label="department.departmentName"
            :value="department.departmentId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="入职日期" prop="employmentDate">
        <el-date-picker clearable
          v-model="queryParams.employmentDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择入职日期">
        </el-date-picker>
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
          v-hasPermi="['Faculty:faculty:add']"
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
          v-hasPermi="['Faculty:faculty:edit']"
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
          v-hasPermi="['Faculty:faculty:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['Faculty:faculty:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="facultyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="关联系统用户ID (sys_user.user_id)" align="center" prop="facultyId" />
      <el-table-column label="工号" align="center" prop="facultyNumber" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="职称" align="center" prop="title">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.usr_prof" :value="scope.row.title"/>
        </template>
      </el-table-column>
      <el-table-column label="研究领域" align="center" prop="researchField" />
      <el-table-column label="所属院系" align="center" prop="departmentName" />
      <el-table-column label="入职日期" align="center" prop="employmentDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.employmentDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="在职状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="getStatusTagType(scope.row.status)">
            {{ getStatusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label=" 论文数" align="center" prop="paperCount" />
      <el-table-column label=" 项目数" align="center" prop="projectCount" />
      <el-table-column label=" 总资金" align="center" prop="totalFunding" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['Faculty:faculty:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['Faculty:faculty:remove']"
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

    <!-- 添加或修改教职工信息及科研统计对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工号" prop="facultyNumber">
          <el-input v-model="form.facultyNumber" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-select v-model="form.title" placeholder="请选择职称">
            <el-option
              v-for="dict in dict.type.usr_prof"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属院系" prop="departmentId">
          <el-select
            v-model="form.departmentId"
            placeholder="请选择所属院系"
            clearable
            filterable
          >
            <el-option
              v-for="department in departmentList"
              :key="department.departmentId"
              :label="department.departmentName"
              :value="department.departmentId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="研究领域" prop="researchField">
          <el-input v-model="form.researchField" placeholder="请输入研究领域" />
        </el-form-item>
        <el-form-item label="入职日期" prop="employmentDate">
          <el-date-picker clearable
            v-model="form.employmentDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择入职日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="在职状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择在职状态">
            <el-option label="在职" value="ACTIVE" />
            <el-option label="离职" value="INACTIVE" />
            <el-option label="退休" value="RETIRED" />
            <el-option label="休假" value="ON_LEAVE" />
          </el-select>
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
import { listFaculty, getFaculty, delFaculty, addFaculty, updateFaculty } from "@/api/Faculty/faculty"
import { listDepartment } from "@/api/Dep/department"

export default {
  name: "Faculty",
  dicts: ['usr_prof'],
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
      // 教职工信息及科研统计表格数据
      facultyList: [],
      // 院系列表
      departmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        facultyNumber: null,
        name: null,
        title: null,
        researchField: null,
        departmentId: null,
        employmentDate: null,
        status: null,
        paperCount: null,
        projectCount: null,
        totalFunding: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        facultyNumber: [
          { required: true, message: "工号不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
    this.loadDepartmentList()
  },
  methods: {
    /** 查询教职工信息及科研统计列表 */
    getList() {
      this.loading = true
      listFaculty(this.queryParams).then(response => {
        this.facultyList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 获取在职状态标签 */
    getStatusLabel(status) {
      const statusMap = {
        'ACTIVE': '在职',
        'INACTIVE': '离职',
        'RETIRED': '退休',
        'ON_LEAVE': '休假'
      }
      return statusMap[status] || status
    },
    /** 获取在职状态标签样式 */
    getStatusTagType(status) {
      const typeMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'danger',
        'RETIRED': 'info',
        'ON_LEAVE': 'warning'
      }
      return typeMap[status] || ''
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        facultyId: null,
        facultyNumber: null,
        name: null,
        title: null,
        researchField: null,
        departmentId: null,
        employmentDate: null,
        status: null,
        paperCount: null,
        projectCount: null,
        totalFunding: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 加载院系列表 */
    loadDepartmentList() {
      listDepartment({ pageNum: 1, pageSize: 9999 }).then(response => {
        this.departmentList = response.rows || []
      })
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.facultyId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加教职工信息及科研统计"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const facultyId = row.facultyId || this.ids
      getFaculty(facultyId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改教职工信息及科研统计"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.facultyId != null) {
            updateFaculty(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addFaculty(this.form).then(response => {
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
      const facultyIds = row.facultyId || this.ids
      this.$modal.confirm('是否确认删除教职工信息及科研统计编号为"' + facultyIds + '"的数据项？').then(function() {
        return delFaculty(facultyIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('Faculty/faculty/export', {
        ...this.queryParams
      }, `faculty_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
