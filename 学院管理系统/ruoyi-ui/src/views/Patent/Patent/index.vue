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
      <el-form-item label="专利类型" prop="patentType">
        <el-select v-model="queryParams.patentType" placeholder="请选择专利类型" clearable @change="handleQuery" style="width: 150px">
          <el-option label="发明专利" value="INVENTION" />
          <el-option label="实用新型" value="UTILITY_MODEL" />
          <el-option label="外观设计" value="DESIGN" />
        </el-select>
      </el-form-item>
      <el-form-item label="专利级别" prop="patentLevel">
        <el-select v-model="queryParams.patentLevel" placeholder="请选择专利级别" clearable @change="handleQuery" style="width: 150px">
          <el-option label="国家级" value="NATIONAL" />
          <el-option label="省级" value="PROVINCIAL" />
          <el-option label="市级" value="CITY" />
        </el-select>
      </el-form-item>
      <el-form-item label="专利状态" prop="patentStatus">
        <el-select v-model="queryParams.patentStatus" placeholder="请选择专利状态" clearable @change="handleQuery" style="width: 150px">
          <el-option label="申请中" value="APPLYING" />
          <el-option label="已授权" value="GRANTED" />
          <el-option label="已过期" value="EXPIRED" />
          <el-option label="已撤回" value="WITHDRAWN" />
        </el-select>
      </el-form-item>
      <el-form-item label="第一作者" prop="firstAuthorName">
        <el-input
          v-model="queryParams.firstAuthorName"
          placeholder="请输入第一作者姓名"
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
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div style="padding: 10px 20px;">
            <!-- 专利摘要 -->
            <div style="margin-bottom: 15px;">
              <div style="margin-bottom: 8px; font-weight: bold; color: #409EFF;">
                <i class="el-icon-document"></i> 专利摘要
              </div>
              <div style="line-height: 1.8; color: #606266; text-indent: 2em; padding: 10px; background-color: #f5f7fa; border-radius: 4px;">
                {{ scope.row.patentAbstract || '暂无摘要' }}
              </div>
            </div>
            <!-- 其他作者 -->
            <div>
              <div style="margin-bottom: 8px; font-weight: bold; color: #67C23A;">
                <i class="el-icon-user"></i> 其他作者 ({{ (scope.row.patentAuthorList || []).length }} 人)
              </div>
              <el-table :data="scope.row.patentAuthorList || []" size="mini" border v-if="(scope.row.patentAuthorList || []).length > 0">
                <el-table-column type="index" label="序号" width="50" align="center" />
                <el-table-column label="作者姓名" min-width="120" align="center">
                  <template slot-scope="authorScope">
                    <span>{{ authorScope.row.authorName }}</span>
                    <el-tag size="mini" :type="authorScope.row.authorType === 'FACULTY' ? 'primary' : 'success'" style="margin-left: 5px;">
                      {{ authorScope.row.authorType === 'FACULTY' ? '教师' : '学生' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="排序" width="80" align="center" prop="authorOrder" />
                <el-table-column label="贡献百分比" width="100" align="center">
                  <template slot-scope="authorScope">
                    <span v-if="authorScope.row.contributionPercent">{{ authorScope.row.contributionPercent }}%</span>
                    <span v-else>-</span>
                  </template>
                </el-table-column>
              </el-table>
              <div v-else style="color: #909399; font-size: 12px; padding: 5px 0;">暂无其他作者</div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <!-- 隐藏ID列 -->
      <el-table-column label="专利号" align="center" prop="patentCode" />
      <el-table-column label="专利名称" align="center" prop="patentName" :show-overflow-tooltip="true" />
      <el-table-column label="专利类型" align="center" prop="patentType" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="getPatentTypeTagType(scope.row.patentType)">
            {{ getPatentTypeLabel(scope.row.patentType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="专利级别" align="center" prop="patentLevel" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="getPatentLevelTagType(scope.row.patentLevel)">
            {{ getPatentLevelLabel(scope.row.patentLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="申请日期" align="center" prop="applicationDate" width="110">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.applicationDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="授权日期" align="center" prop="grantDate" width="110">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.grantDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到期日期" align="center" prop="expiryDate" width="110">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expiryDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="专利状态" align="center" prop="patentStatus" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="getPatentStatusTagType(scope.row.patentStatus)">
            {{ getPatentStatusLabel(scope.row.patentStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="第一作者" align="center" min-width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.firstAuthorName }} ({{ scope.row.firstAuthorType === 'FACULTY' ? '教师' : '学生' }})</span>
        </template>
      </el-table-column>
      <el-table-column label="所属院系" align="center" prop="departmentName" min-width="120" />
      <el-table-column label="附件链接" align="center" prop="attachmentUrl" />
      <el-table-column v-if="checkPermi(['Patent:Patent:remove'])" label="操作" align="center" class-name="small-padding fixed-width">
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
        <el-form-item label="专利类型" prop="patentType">
          <el-select v-model="form.patentType" placeholder="请选择专利类型" style="width: 100%">
            <el-option label="发明专利" value="INVENTION" />
            <el-option label="实用新型" value="UTILITY_MODEL" />
            <el-option label="外观设计" value="DESIGN" />
          </el-select>
        </el-form-item>
        <el-form-item label="专利级别" prop="patentLevel">
          <el-select v-model="form.patentLevel" placeholder="请选择专利级别" style="width: 100%">
            <el-option label="国家级" value="NATIONAL" />
            <el-option label="省级" value="PROVINCIAL" />
            <el-option label="市级" value="CITY" />
          </el-select>
        </el-form-item>
        <el-form-item label="专利状态" prop="patentStatus">
          <el-select v-model="form.patentStatus" placeholder="请选择专利状态" style="width: 100%">
            <el-option label="申请中" value="APPLYING" />
            <el-option label="已授权" value="GRANTED" />
            <el-option label="已过期" value="EXPIRED" />
            <el-option label="已撤回" value="WITHDRAWN" />
          </el-select>
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
        <el-form-item label="所属院系" prop="departmentName">
          <el-input v-model="form.departmentName" placeholder="自动填充" disabled />
        </el-form-item>
        <el-form-item label="附件链接" prop="attachmentUrl">
          <el-input v-model="form.attachmentUrl" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-divider content-position="center">其他作者信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddPatentAuthor">添加作者</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeletePatentAuthor">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="patentAuthorList" @selection-change="handlePatentAuthorSelectionChange" ref="patentAuthor" size="small">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column type="index" label="序号" width="50" />
          <el-table-column label="作者类型" width="100">
            <template slot-scope="scope">
              <el-select v-model="scope.row.authorType" placeholder="类型" size="small" @change="handlePatentAuthorTypeChange(scope.row)">
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
                remote
                :remote-method="(query) => remoteSearchPatentAuthor(query, scope.row)"
                size="small"
                style="width: 100%"
              >
                <el-option
                  v-for="author in scope.row.authorOptions || []"
                  :key="author.userId"
                  :label="author.name + ' (' + (author.userNumber) + ')'"
                  :value="author.userId"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="排序" width="120" align="center">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.authorOrder" :min="1" :max="99" size="small" style="width: 100px" controls-position="right" />
            </template>
          </el-table-column>
          <el-table-column label="贡献百分比" min-width="120" align="center">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.contributionPercent" :min="0" :max="100" size="small" style="width: 100px" controls-position="right" />
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
import { listFaculty } from "@/api/Faculty/faculty"
import { listStudent } from "@/api/Stu/student"
import { checkPermi } from "@/utils/permission"

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
      // 教师列表
      facultyList: [],
      // 学生列表
      studentList: [],
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
        firstAuthorName: null,
        departmentId: null,
        departmentName: null,
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
          { required: true, message: "专利类型不能为空", trigger: "change" }
        ],
        patentLevel: [
          { required: true, message: "专利级别不能为空", trigger: "change" }
        ],
        patentStatus: [
          { required: true, message: "专利状态不能为空", trigger: "change" }
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
    this.loadFacultyList()
    this.loadStudentList()
  },
  methods: {
    checkPermi,
    /** 获取专利类型标签 */
    getPatentTypeLabel(type) {
      const typeMap = {
        'INVENTION': '发明专利',
        'UTILITY_MODEL': '实用新型',
        'DESIGN': '外观设计'
      }
      return typeMap[type] || type
    },
    /** 获取专利类型标签样式 */
    getPatentTypeTagType(type) {
      const typeMap = {
        'INVENTION': 'success',
        'UTILITY_MODEL': 'primary',
        'DESIGN': 'warning'
      }
      return typeMap[type] || ''
    },
    /** 获取专利级别标签 */
    getPatentLevelLabel(level) {
      const levelMap = {
        'NATIONAL': '国家级',
        'PROVINCIAL': '省级',
        'CITY': '市级'
      }
      return levelMap[level] || level
    },
    /** 获取专利级别标签样式 */
    getPatentLevelTagType(level) {
      const levelMap = {
        'NATIONAL': 'danger',
        'PROVINCIAL': 'warning',
        'CITY': 'primary'
      }
      return levelMap[level] || ''
    },
    /** 获取专利状态标签 */
    getPatentStatusLabel(status) {
      const statusMap = {
        'APPLYING': '申请中',
        'GRANTED': '已授权',
        'EXPIRED': '已过期',
        'WITHDRAWN': '已撤回'
      }
      return statusMap[status] || status
    },
    /** 获取专利状态标签样式 */
    getPatentStatusTagType(status) {
      const statusMap = {
        'APPLYING': 'primary',
        'GRANTED': 'success',
        'EXPIRED': 'info',
        'WITHDRAWN': 'danger'
      }
      return statusMap[status] || ''
    },
    /** 查询专利信息列表 */
    getList() {
      this.loading = true
      listPatent(this.queryParams).then(response => {
        this.PatentList = response.rows
        this.total = response.total
        this.loading = false
      })
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
      this.form.departmentName = null
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
        patentType: "INVENTION",
        patentLevel: "NATIONAL",
        applicationDate: null,
        grantDate: null,
        expiryDate: null,
        patentStatus: "APPLYING",
        firstAuthorType: "FACULTY",
        firstAuthorId: null,
        firstAuthorName: null,
        departmentId: null,
        departmentName: null,
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
    /** 作者类型变更 */
    handlePatentAuthorTypeChange(row) {
      row.authorId = null
      this.$set(row, 'authorOptions', [])
    },
    /** 远程搜索作者 */
    remoteSearchPatentAuthor(query, row) {
      if (query.length < 1) {
        this.$set(row, 'authorOptions', [])
        return
      }
      if (row.authorType === 'FACULTY') {
        listFaculty({ pageNum: 1, pageSize: 10, name: query }).then(res => {
          const options = (res.rows || []).map(item => ({
            userId: item.facultyId,
            name: item.name,
            userNumber: item.facultyNumber
          }))
          this.$set(row, 'authorOptions', options)
        }).catch(err => {
          console.error('搜索教师失败:', err)
          this.$set(row, 'authorOptions', [])
        })
      } else if (row.authorType === 'STUDENT') {
        listStudent({ pageNum: 1, pageSize: 10, name: query }).then(res => {
          const options = (res.rows || []).map(item => ({
            userId: item.studentId,
            name: item.name,
            userNumber: item.studentNumber
          }))
          this.$set(row, 'authorOptions', options)
        }).catch(err => {
          console.error('搜索学生失败:', err)
          this.$set(row, 'authorOptions', [])
        })
      }
    },
    /** 专利与作者的多对多关系添加按钮操作 */
    handleAddPatentAuthor() {
      let obj = {}
      obj.authorType = 'FACULTY'
      obj.authorId = null
      obj.authorOrder = this.patentAuthorList.length + 2
      obj.contributionPercent = null
      obj.authorOptions = []
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
