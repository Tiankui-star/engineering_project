<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="专利号" prop="patentCode">
        <el-input
          v-model="queryParams.patentCode"
          placeholder="请输入专利号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="专利名称" prop="patentName">
        <el-input
          v-model="queryParams.patentName"
          placeholder="请输入专利名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请日期" prop="applicationDate">
        <el-date-picker clearable
          v-model="queryParams.applicationDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择申请日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="授权日期" prop="grantDate">
        <el-date-picker clearable
          v-model="queryParams.grantDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择授权日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="到期日期" prop="expiryDate">
        <el-date-picker clearable
          v-model="queryParams.expiryDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择到期日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="第一作者ID" prop="firstAuthorId">
        <el-input
          v-model="queryParams.firstAuthorId"
          placeholder="请输入第一作者ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属院系" prop="departmentId">
        <el-input
          v-model="queryParams.departmentId"
          placeholder="请输入所属院系"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['Patent:Patent:add']"
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
          v-hasPermi="['Patent:Patent:edit']"
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
          v-hasPermi="['Patent:Patent:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['Patent:Patent:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="PatentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="" align="center" prop="patentId" />
      <el-table-column label="专利号" align="center" prop="patentCode" />
      <el-table-column label="专利名称" align="center" prop="patentName" />
      <el-table-column label="专利摘要" align="center" prop="patentAbstract" />
      <el-table-column label="专利类型：发明/实用新型/外观设计" align="center" prop="patentType" />
      <el-table-column label="专利级别" align="center" prop="patentLevel" />
      <el-table-column label="申请日期" align="center" prop="applicationDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.applicationDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="授权日期" align="center" prop="grantDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.grantDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到期日期" align="center" prop="expiryDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expiryDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="专利状态" align="center" prop="patentStatus" />
      <el-table-column label="第一作者类型" align="center" prop="firstAuthorType" />
      <el-table-column label="第一作者ID" align="center" prop="firstAuthorId" />
      <el-table-column label="所属院系" align="center" prop="departmentId" />
      <el-table-column label="附件链接" align="center" prop="attachmentUrl" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['Patent:Patent:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['Patent:Patent:remove']"
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

    <!-- 添加或修改专利信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="专利号" prop="patentCode">
          <el-input v-model="form.patentCode" placeholder="请输入专利号" />
        </el-form-item>
        <el-form-item label="专利名称" prop="patentName">
          <el-input v-model="form.patentName" placeholder="请输入专利名称" />
        </el-form-item>
        <el-form-item label="专利摘要" prop="patentAbstract">
          <el-input v-model="form.patentAbstract" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="申请日期" prop="applicationDate">
          <el-date-picker clearable
            v-model="form.applicationDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择申请日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="授权日期" prop="grantDate">
          <el-date-picker clearable
            v-model="form.grantDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择授权日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="到期日期" prop="expiryDate">
          <el-date-picker clearable
            v-model="form.expiryDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择到期日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="第一作者ID" prop="firstAuthorId">
          <el-input v-model="form.firstAuthorId" placeholder="请输入第一作者ID" />
        </el-form-item>
        <el-form-item label="所属院系" prop="departmentId">
          <el-input v-model="form.departmentId" placeholder="请输入所属院系" />
        </el-form-item>
        <el-form-item label="附件链接" prop="attachmentUrl">
          <el-input v-model="form.attachmentUrl" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-divider content-position="center">专利与作者的多对多关系信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddPatentAuthor">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeletePatentAuthor">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="patentAuthorList" :row-class-name="rowPatentAuthorIndex" @selection-change="handlePatentAuthorSelectionChange" ref="patentAuthor">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="专利ID" prop="patentId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.patentId" placeholder="请输入专利ID" />
            </template>
          </el-table-column>
          <el-table-column label="作者类型" prop="authorType" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.authorType" placeholder="请选择作者类型">
                <el-option label="请选择字典生成" value="" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="作者排序" prop="authorOrder" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.authorOrder" placeholder="请输入作者排序" />
            </template>
          </el-table-column>
          <el-table-column label="贡献百分比" prop="contributionPercent" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.contributionPercent" placeholder="请输入贡献百分比" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPatent, getPatent, delPatent, addPatent, updatePatent } from "@/api/Patent/Patent"

export default {
  name: "Patent",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedPatentAuthor: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 专利信息表格数据
      PatentList: [],
      // 专利与作者的多对多关系表格数据
      patentAuthorList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patentCode: null,
        patentName: null,
        patentAbstract: null,
        patentType: null,
        patentLevel: null,
        applicationDate: null,
        grantDate: null,
        expiryDate: null,
        patentStatus: null,
        firstAuthorType: null,
        firstAuthorId: null,
        departmentId: null,
        attachmentUrl: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        patentCode: [
          { required: true, message: "专利号不能为空", trigger: "blur" }
        ],
        patentName: [
          { required: true, message: "专利名称不能为空", trigger: "blur" }
        ],
        patentType: [
          { required: true, message: "专利类型：发明/实用新型/外观设计不能为空", trigger: "change" }
        ],
        firstAuthorType: [
          { required: true, message: "第一作者类型不能为空", trigger: "change" }
        ],
        firstAuthorId: [
          { required: true, message: "第一作者ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询专利信息列表 */
    getList() {
      this.loading = true
      listPatent(this.queryParams).then(response => {
        this.PatentList = response.rows
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
        patentId: null,
        patentCode: null,
        patentName: null,
        patentAbstract: null,
        patentType: null,
        patentLevel: null,
        applicationDate: null,
        grantDate: null,
        expiryDate: null,
        patentStatus: null,
        firstAuthorType: null,
        firstAuthorId: null,
        departmentId: null,
        attachmentUrl: null
      }
      this.patentAuthorList = []
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
      this.ids = selection.map(item => item.patentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加专利信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const patentId = row.patentId || this.ids
      getPatent(patentId).then(response => {
        this.form = response.data
        this.patentAuthorList = response.data.patentAuthorList
        this.open = true
        this.title = "修改专利信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.patentAuthorList = this.patentAuthorList
          if (this.form.patentId != null) {
            updatePatent(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPatent(this.form).then(response => {
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
      const patentIds = row.patentId || this.ids
      this.$modal.confirm('是否确认删除专利信息编号为"' + patentIds + '"的数据项？').then(function() {
        return delPatent(patentIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
	/** 专利与作者的多对多关系序号 */
    rowPatentAuthorIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 专利与作者的多对多关系添加按钮操作 */
    handleAddPatentAuthor() {
      let obj = {}
      obj.patentId = ""
      obj.authorType = ""
      obj.authorOrder = ""
      obj.contributionPercent = ""
      this.patentAuthorList.push(obj)
    },
    /** 专利与作者的多对多关系删除按钮操作 */
    handleDeletePatentAuthor() {
      if (this.checkedPatentAuthor.length == 0) {
        this.$modal.msgError("请先选择要删除的专利与作者的多对多关系数据")
      } else {
        const patentAuthorList = this.patentAuthorList
        const checkedPatentAuthor = this.checkedPatentAuthor
        this.patentAuthorList = patentAuthorList.filter(function(item) {
          return checkedPatentAuthor.indexOf(item.index) == -1
        })
      }
    },
    /** 复选框选中数据 */
    handlePatentAuthorSelectionChange(selection) {
      this.checkedPatentAuthor = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('Patent/Patent/export', {
        ...this.queryParams
      }, `Patent_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
