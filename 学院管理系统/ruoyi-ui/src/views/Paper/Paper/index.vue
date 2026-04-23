<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="论文标题" prop="paperTitle">
        <el-input
          v-model="queryParams.paperTitle"
          placeholder="请输入论文标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="期刊名称" prop="journalName">
        <el-input
          v-model="queryParams.journalName"
          placeholder="请输入期刊名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发表日期" prop="publicationDate">
        <el-date-picker clearable
          v-model="queryParams.publicationDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择发表日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="发表级别" prop="publicationLevel">
        <el-select v-model="queryParams.publicationLevel" placeholder="请选择发表级别" clearable>
          <el-option
            v-for="dict in publicationLevelOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
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
          v-hasPermi="['Paper:Paper:add']"
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
          v-hasPermi="['Paper:Paper:edit']"
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
          v-hasPermi="['Paper:Paper:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['Paper:Paper:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="PaperList" @selection-change="handleSelectionChange">
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div style="padding: 10px 20px;">
            <div style="margin-bottom: 8px; font-weight: bold; color: #409EFF;">
              <i class="el-icon-document"></i> 论文摘要
            </div>
            <div style="line-height: 1.8; color: #606266; text-indent: 2em; padding: 10px; background-color: #f5f7fa; border-radius: 4px;">
              {{ scope.row.paperAbstract || '暂无摘要' }}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="论文标题" align="center" prop="paperTitle" :show-overflow-tooltip="true" />
      <el-table-column label="期刊名称" align="center" prop="journalName" />
      <el-table-column label="发表日期" align="center" prop="publicationDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.publicationDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发表级别" align="center" prop="publicationLevel" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="getPublicationLevelTagType(scope.row.publicationLevel)">
            {{ formatPublicationLevel(scope.row.publicationLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="一作姓名" align="center" prop="firstAuthorName" width="100" />
      <el-table-column label="其他作者" align="center" min-width="200" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ formatOtherAuthors(scope.row.paperAuthorList) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属院系" align="center" prop="departmentName" width="120" />
      <el-table-column v-if="checkPermi(['Paper:Paper:remove'])" label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['Paper:Paper:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['Paper:Paper:remove']"
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

    <!-- 添加或修改学术论文信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="论文标题" prop="paperTitle">
          <el-input v-model="form.paperTitle" placeholder="请输入论文标题" />
        </el-form-item>
        <el-form-item label="摘要" prop="paperAbstract">
          <el-input v-model="form.paperAbstract" type="textarea" :rows="3" placeholder="请输入摘要" />
        </el-form-item>
        <el-form-item label="期刊名称" prop="journalName">
          <el-input v-model="form.journalName" placeholder="请输入期刊名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="卷期" prop="volumeIssue">
              <el-input v-model="form.volumeIssue" placeholder="请输入卷期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="页码" prop="pageNumbers">
              <el-input v-model="form.pageNumbers" placeholder="请输入页码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="发表日期" prop="publicationDate">
              <el-date-picker clearable style="width: 100%"
                v-model="form.publicationDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择发表日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发表级别" prop="publicationLevel">
              <el-select v-model="form.publicationLevel" placeholder="请选择发表级别" style="width: 100%">
                <el-option
                  v-for="dict in publicationLevelOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="DOI号" prop="doi">
          <el-input v-model="form.doi" placeholder="请输入DOI号" />
        </el-form-item>
        <el-form-item label="第一作者类型" prop="firstAuthorType">
          <el-radio-group v-model="form.firstAuthorType" @change="handleAuthorTypeChange">
            <el-radio label="FACULTY">教师</el-radio>
            <el-radio label="STUDENT">学生</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="第一作者" prop="firstAuthorId">
          <el-select
            v-model="form.firstAuthorId"
            :placeholder="form.firstAuthorType === 'FACULTY' ? '请选择教师' : '请选择学生'"
            clearable
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="author in form.firstAuthorType === 'FACULTY' ? facultyList : studentList"
              :key="author.facultyId || author.studentId"
              :label="author.name + ' (' + (author.facultyNumber || author.studentNumber) + ')'"
              :value="author.facultyId || author.studentId"
            />
          </el-select>
        </el-form-item>
        <el-divider content-position="center">其他作者信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddAuthor">添加作者</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteAuthor">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="paperAuthorList" @selection-change="handleAuthorSelectionChange" ref="authorTable" size="small">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column type="index" label="序号" width="50" />
          <el-table-column label="作者类型" width="100">
            <template slot-scope="scope">
              <el-select v-model="scope.row.authorType" placeholder="类型" size="small">
                <el-option label="教师" value="FACULTY" />
                <el-option label="学生" value="STUDENT" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="作者" min-width="180">
            <template slot-scope="scope">
              <el-select
                v-model="scope.row.authorId"
                placeholder="请选择作者"
                clearable
                filterable
                size="small"
                style="width: 100%"
              >
                <el-option
                  v-for="author in scope.row.authorType === 'FACULTY' ? facultyList : studentList"
                  :key="author.facultyId || author.studentId"
                  :label="author.name + ' (' + (author.facultyNumber || author.studentNumber) + ')'"
                  :value="author.facultyId || author.studentId"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="排序" width="120" align="center">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.authorOrder" :min="1" :max="99" size="small" style="width: 100px" controls-position="right" />
            </template>
          </el-table-column>
          <el-table-column label="贡献描述" min-width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.contributionDescription" placeholder="贡献描述" size="small" />
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
import { listPaper, getPaper, delPaper, addPaper, updatePaper } from "@/api/Paper/Paper"
import { listFaculty } from "@/api/Faculty/faculty"
import { listStudent } from "@/api/Stu/student"
import { checkPermi } from "@/utils/permission"

export default {
  name: "Paper",
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
      // 学术论文信息表格数据
      PaperList: [],
      // 教师列表
      facultyList: [],
      // 学生列表
      studentList: [],
      // 论文作者列表
      paperAuthorList: [],
      // 选中的作者
      checkedAuthors: [],
      // 发表级别选项
      publicationLevelOptions: [
        { label: "核心A", value: "CORE_A" },
        { label: "核心B", value: "CORE_B" },
        { label: "核心C", value: "CORE_C" },
        { label: "SCI", value: "SCI" },
        { label: "EI", value: "EI" },
        { label: "其他", value: "OTHER" }
      ],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        paperTitle: null,
        paperAbstract: null,
        journalName: null,
        publicationDate: null,
        publicationLevel: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        paperTitle: [
          { required: true, message: "论文标题不能为空", trigger: "blur" }
        ],
        firstAuthorId: [
          { required: true, message: "第一作者不能为空", trigger: "change" }
        ],
        doi: [
          { pattern: /^10\.\d{4,}\/\S+$/, message: "DOI格式不正确，应为 10.xxxx/xxx 格式", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
    this.loadFacultyList()
    this.loadStudentList()
  },
  methods: {
    checkPermi,
    /** 查询学术论文信息列表 */
    getList() {
      this.loading = true
      listPaper(this.queryParams).then(response => {
        this.PaperList = response.rows
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
        paperId: null,
        paperTitle: null,
        paperAbstract: null,
        journalName: null,
        volumeIssue: null,
        pageNumbers: null,
        publicationDate: null,
        publicationLevel: null,
        doi: null,
        citationCount: null,
        firstAuthorType: "FACULTY",
        firstAuthorId: null,
        departmentId: null,
        departmentName: null
      }
      this.paperAuthorList = []
      this.checkedAuthors = []
      this.resetForm("form")
    },
    /** 加载教师列表 */
    loadFacultyList() {
      listFaculty({ pageNum: 1, pageSize: 9999 }).then(response => {
        this.facultyList = response.rows || []
      })
    },
    /** 加载学生列表 */
    loadStudentList() {
      listStudent({ pageNum: 1, pageSize: 9999 }).then(response => {
        this.studentList = response.rows || []
      })
    },
    /** 作者类型变化 */
    handleAuthorTypeChange() {
      this.form.firstAuthorId = null
      this.form.departmentId = null
    },
    /** 添加作者 */
    handleAddAuthor() {
      this.paperAuthorList.push({
        authorshipId: null,
        paperId: null,
        authorType: 'FACULTY',
        authorId: null,
        authorOrder: this.paperAuthorList.length + 2,
        contributionDescription: ''
      })
    },
    /** 删除作者 */
    handleDeleteAuthor() {
      if (this.checkedAuthors.length === 0) {
        this.$modal.msgError("请先选择要删除的作者")
        return
      }
      this.paperAuthorList = this.paperAuthorList.filter(item => !this.checkedAuthors.includes(item))
      this.checkedAuthors = []
    },
    /** 作者选择变化 */
    handleAuthorSelectionChange(selection) {
      this.checkedAuthors = selection
    },
    /** 格式化发表级别 */
    formatPublicationLevel(level) {
      const option = this.publicationLevelOptions.find(item => item.value === level)
      return option ? option.label : level
    },
    /** 获取发表级别标签样式 */
    getPublicationLevelTagType(level) {
      const typeMap = {
        'SCI': 'danger',
        'EI': 'warning',
        'CSSCI': 'success',
        '核心期刊': 'primary',
        '普刊': 'info'
      }
      return typeMap[level] || ''
    },
    /** 格式化其他作者 */
    formatOtherAuthors(authorList) {
      if (!authorList || authorList.length === 0) {
        return '-'
      }
      // 过滤掉一作（authorOrder为1的），并按排序排序
      const otherAuthors = authorList.filter(author => author.authorOrder !== 1).sort((a, b) => a.authorOrder - b.authorOrder)
      if (otherAuthors.length === 0) {
        return '-'
      }
      // 返回作者姓名列表，用逗号分隔
      return otherAuthors.map(author => author.authorName || '').filter(name => name).join('，')
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
      this.ids = selection.map(item => item.paperId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加学术论文信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const paperId = row.paperId || this.ids
      getPaper(paperId).then(response => {
        this.form = response.data
        // 加载作者列表
        this.paperAuthorList = response.data.paperAuthorList || []
        this.open = true
        this.title = "修改学术论文信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 构建作者列表（包括一作和其他作者）
          const authorList = []
          
          // 添加一作
          if (this.form.firstAuthorId) {
            authorList.push({
              authorType: this.form.firstAuthorType,
              authorId: this.form.firstAuthorId,
              authorOrder: 1,
              contributionDescription: '第一作者'
            })
          }
          
          // 添加其他作者（排除一作，即authorOrder不为1的）
          this.paperAuthorList.forEach(author => {
            if (author.authorId && author.authorOrder !== 1) {
              authorList.push({
                authorType: author.authorType,
                authorId: author.authorId,
                authorOrder: author.authorOrder,
                contributionDescription: author.contributionDescription
              })
            }
          })
          
          // 设置到表单
          this.form.paperAuthorList = authorList
          
          if (this.form.paperId != null) {
            updatePaper(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPaper(this.form).then(response => {
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
      const paperIds = row.paperId || this.ids
      this.$modal.confirm('是否确认删除学术论文信息编号为"' + paperIds + '"的数据项？').then(function() {
        return delPaper(paperIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('Paper/Paper/export', {
        ...this.queryParams
      }, `Paper_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
