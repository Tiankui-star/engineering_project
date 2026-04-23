<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="学号" prop="studentNumber">
        <el-input
          v-model="queryParams.studentNumber"
          placeholder="请输入学号"
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
      <el-form-item label="身份证号" prop="idCard">
        <el-input
          v-model="queryParams.idCard"
          placeholder="请输入身份证号"
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
      <el-form-item label="入学日期" prop="enrollmentDate">
        <el-date-picker clearable
          v-model="queryParams.enrollmentDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择入学日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="获奖数" prop="awardCount">
        <el-input
          v-model="queryParams.awardCount"
          placeholder="请输入获奖数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="参与论文数" prop="paperCount" label-width="85px">
        <el-input
          v-model="queryParams.paperCount"
          placeholder="请输入参与论文数"
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
          v-hasPermi="['Stu:student:add']"
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
          v-hasPermi="['Stu:student:edit']"
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
          v-hasPermi="['Stu:student:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['Stu:student:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange" @expand-change="handleExpandChange" :row-key="getRowKey">
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div style="padding: 10px 20px;">
            <div style="margin-bottom: 10px; font-weight: bold; color: #409EFF;">
              <i class="el-icon-trophy"></i> 获奖记录 (共 {{ (scope.row.studentAwardList || []).length }} 条)
            </div>
            <el-table :data="scope.row.studentAwardList || []" size="mini" border v-if="(scope.row.studentAwardList || []).length > 0">
              <el-table-column type="index" label="序号" width="60" align="center" />
              <el-table-column label="奖项名称" prop="awardName" min-width="150" show-overflow-tooltip />
              <el-table-column label="获奖日期" prop="awardDate" width="120" align="center">
                <template slot-scope="awardScope">
                  <span>{{ parseTime(awardScope.row.awardDate, '{y}-{m}-{d}') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="颁发机构" prop="issuingOrganization" min-width="150" show-overflow-tooltip />
              <el-table-column label="奖项等级" prop="awardLevel" width="100" align="center">
                <template slot-scope="awardScope">
                  <el-tag :type="getAwardLevelTagType(awardScope.row.awardLevel)" size="small">
                    {{ getAwardLevelLabel(awardScope.row.awardLevel) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="备注" prop="description" min-width="150" show-overflow-tooltip />
              <el-table-column label="指导老师" width="120" align="center">
                <template slot-scope="awardScope">
                  <span>{{ getFacultyName(awardScope.row.supervisorFacultyId) }}</span>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-else description="暂无获奖记录" :image-size="60"></el-empty>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学号" align="center" prop="studentNumber" min-width="120" />
      <el-table-column label="姓名" align="center" prop="name" min-width="100" />
      <el-table-column label="性别" align="center" prop="gender" width="80">
        <template slot-scope="scope">
          <span>{{ getGenderLabel(scope.row.gender) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="身份证号" align="center" prop="idCard" min-width="180" />
      <el-table-column label="所属院系" align="center" min-width="150">
        <template slot-scope="scope">
          <span>{{ getDepartmentName(scope.row.departmentId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="入学日期" align="center" prop="enrollmentDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.enrollmentDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="学籍状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStudentStatusTagType(scope.row.status)" size="small">
            {{ getStudentStatusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="获奖数" align="center" prop="awardCount" width="80" />
      <el-table-column label="参与论文数" align="center" prop="paperCount" width="100" />
      <el-table-column label="参与项目数" align="center" prop="projectCount" width="100" />
      <el-table-column v-if="checkPermi(['Stu:student:remove'])" label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['Stu:student:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['Stu:student:remove']"
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

    <!-- 添加或修改学生基本信息及成就统计对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="学号" prop="studentNumber">
          <el-input v-model="form.studentNumber" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别" clearable style="width: 100%">
            <el-option label="男" value="M" />
            <el-option label="女" value="F" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input
            v-model="form.idCard"
            placeholder="请输入18位身份证号，出生日期将自动生成"
            maxlength="18"
            @input="handleIdCardChange"
          />
        </el-form-item>
        <el-form-item label="出生日期">
          <el-input
            v-model="form.birthDate"
            placeholder="由身份证号自动生成"
            disabled
            style="width: 100%"
          />
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
        <el-form-item label="入学日期" prop="enrollmentDate">
          <el-date-picker clearable
            v-model="form.enrollmentDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择入学日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="学籍状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择学籍状态" clearable style="width: 100%">
            <el-option label="在读" value="ENROLLED" />
            <el-option label="已毕业" value="GRADUATED" />
            <el-option label="休学" value="SUSPENDED" />
          </el-select>
        </el-form-item>
        <el-divider content-position="center">学生获得的各类奖项记录信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddStudentAward">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteStudentAward">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="studentAwardList" @selection-change="handleStudentAwardSelectionChange" ref="studentAward">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column type="index" label="序号" width="50" />
          <el-table-column label="奖项名称" prop="awardName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.awardName" placeholder="请输入奖项名称" />
            </template>
          </el-table-column>
          <el-table-column label="获奖日期" prop="awardDate" width="180">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.awardDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择获奖日期" />
            </template>
          </el-table-column>
          <el-table-column label="颁发机构" prop="issuingOrganization" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.issuingOrganization" placeholder="请输入颁发机构" />
            </template>
          </el-table-column>
          <el-table-column label="奖项等级" prop="awardLevel" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.awardLevel" placeholder="请选择奖项等级" clearable>
                <el-option
                  v-for="dict in awardLevelDict"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="description" width="180">
            <template slot-scope="scope">
              <el-input v-model="scope.row.description" placeholder="请输入备注" />
            </template>
          </el-table-column>
          <el-table-column label="指导老师" prop="supervisorFacultyId" width="150">
            <template slot-scope="scope">
              <el-select
                v-model="scope.row.supervisorFacultyId"
                placeholder="请选择指导老师"
                clearable
                filterable
                popper-append-to-body
                :popper-class="'faculty-select-popper'"
              >
                <el-option
                  v-for="faculty in facultyList"
                  :key="faculty.facultyId"
                  :label="faculty.name"
                  :value="faculty.facultyId"
                />
              </el-select>
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
import { listStudent, getStudent, delStudent, addStudent, updateStudent } from "@/api/Stu/student"
import { listDepartment } from "@/api/Dep/department"
import { listFaculty } from "@/api/Faculty/faculty"
import { checkPermi } from "@/utils/permission"

export default {
  name: "Student",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedStudentAward: [],
      // 院系列表
      departmentList: [],
      // 教师列表
      facultyList: [],
      // 远程搜索教师列表
      filteredFacultyList: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 学生基本信息及成就统计表格数据
      studentList: [],
      // 学生获得的各类奖项记录表格数据
      studentAwardList: [],
      // 奖项等级字典（与数据库 enum 对应）
      awardLevelDict: [
        { label: "国家级", value: "NATIONAL" },
        { label: "省级", value: "PROVINCIAL" },
        { label: "校级", value: "UNIVERSITY" },
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
        studentNumber: null,
        name: null,
        gender: null,
        idCard: null,
        departmentId: null,
        enrollmentDate: null,
        status: null,
        awardCount: null,
        paperCount: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        studentNumber: [
          { required: true, message: "学号不能为空", trigger: "blur" },
          { pattern: /^\d{10}$/, message: "学号必须为10位数字", trigger: "blur" }
        ],
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        idCard: [
          { pattern: /^\d{17}[\dXx]$/, message: "身份证号须为18位，末位可为X", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
    this.loadDepartmentList()
    this.loadFacultyList()
  },
  methods: {
    checkPermi,
    /** 查询学生基本信息及成就统计列表 */
    getList() {
      this.loading = true
      listStudent(this.queryParams).then(response => {
        this.studentList = response.rows
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
        studentId: null,
        studentNumber: null,
        name: null,
        birthDate: null,
        gender: null,
        idCard: null,
        departmentId: null,
        enrollmentDate: null,
        status: null,
        awardCount: null,
        paperCount: null
      }
      this.studentAwardList = []
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
    getDepartmentName(departmentId) {
      if (!departmentId) return '-'
      const dept = this.departmentList.find(item => item.departmentId === departmentId)
      return dept ? dept.departmentName : '-'
    },
    /** 加载教师列表 */
    loadFacultyList() {
      listFaculty({ pageNum: 1, pageSize: 9999 }).then(response => {
        this.facultyList = response.rows || []
        this.filteredFacultyList = this.facultyList
      })
    },
    /** 指导老师模糊搜索 */
    filterFaculty(query) {
      if (!query) {
        this.filteredFacultyList = this.facultyList
        return
      }
      const keyword = query.trim().toLowerCase()
      this.filteredFacultyList = this.facultyList.filter(faculty => {
        return faculty.name && faculty.name.toLowerCase().includes(keyword)
      })
    },
    /** 指导老师选择框获取焦点时重置列表 */
    handleFacultySelectFocus() {
      this.filteredFacultyList = this.facultyList
    },
    /** 指导老师过滤变化事件 */
    handleFacultyFilterChange(query) {
      this.filterFaculty(query)
    },
    /** 身份证号输入时自动提取出生日期
     *  中国居民身份证第7-14位为出生日期（YYYYMMDD）
     */
    handleIdCardChange(val) {
      const id = (val || '').trim()
      if (/^\d{17}[\dXx]$/.test(id)) {
        const year  = id.substring(6, 10)
        const month = id.substring(10, 12)
        const day   = id.substring(12, 14)
        this.form.birthDate = `${year}-${month}-${day}`
      } else {
        this.form.birthDate = null
      }
    },
    /** 重置按鈕操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.studentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加学生基本信息及成就统计"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const studentId = row.studentId || this.ids
      getStudent(studentId).then(response => {
        this.form = response.data
        this.studentAwardList = response.data.studentAwardList || []
        this.open = true
        this.title = "修改学生基本信息及成就统计"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const invalidAward = this.studentAwardList.find(item => {
            return item.supervisorFacultyId && !this.facultyList.some(faculty => faculty.facultyId === item.supervisorFacultyId)
          })
          if (invalidAward) {
            this.$modal.msgError("指导老师不存在，请重新选择")
            return
          }
          this.form.studentAwardList = this.studentAwardList
          if (this.form.studentId != null) {
            updateStudent(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addStudent(this.form).then(response => {
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
      const studentIds = row.studentId || this.ids
      this.$modal.confirm('是否确认删除学生基本信息及成就统计编号为"' + studentIds + '"的数据项？').then(function() {
        return delStudent(studentIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },

    /** 学生获得的各类奖项记录添加按钮操作 */
    handleAddStudentAward() {
      this.studentAwardList.push({
        awardName: "",
        awardDate: "",
        issuingOrganization: "",
        awardLevel: "",
        description: "",
        supervisorFacultyId: ""
      })
    },
    /** 学生获得的各类奖项记录删除按钮操作 */
    handleDeleteStudentAward() {
      if (!this.checkedStudentAward.length) {
        this.$modal.msgError("请先选择要删除的学生获得的各类奖项记录数据")
        return
      }
      this.studentAwardList = this.studentAwardList.filter(item => !this.checkedStudentAward.includes(item))
      this.checkedStudentAward = []
    },
    /** 复选框选中数据 */
    handleStudentAwardSelectionChange(selection) {
      this.checkedStudentAward = selection
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('Stu/student/export', {
        ...this.queryParams
      }, `student_${new Date().getTime()}.xlsx`)
    },
    /** 获取行唯一标识 */
    getRowKey(row) {
      return row.studentId
    },
    /** 表格展开事件 - 加载学生获奖记录 */
    handleExpandChange(row, expandedRows) {
      // 如果展开且尚未加载获奖记录，则加载
      if (expandedRows.find(item => item.studentId === row.studentId)) {
        if (!row.studentAwardList && !row.loading) {
          this.$set(row, 'loading', true)
          getStudent(row.studentId).then(response => {
            this.$set(row, 'studentAwardList', response.data.studentAwardList || [])
            this.$set(row, 'loading', false)
          }).catch(() => {
            this.$set(row, 'studentAwardList', [])
            this.$set(row, 'loading', false)
          })
        }
      }
    },
    /** 获取奖项等级标签类型 */
    getAwardLevelTagType(level) {
      const typeMap = {
        'NATIONAL': 'danger',
        'PROVINCIAL': 'warning',
        'UNIVERSITY': 'success',
        'OTHER': 'info'
      }
      return typeMap[level] || 'info'
    },
    /** 获取奖项等级中文标签 */
    getAwardLevelLabel(level) {
      const item = this.awardLevelDict.find(d => d.value === level)
      return item ? item.label : level
    },
    /** 获取教师姓名 */
    getFacultyName(facultyId) {
      if (!facultyId) return '-'
      const faculty = this.facultyList.find(item => item.facultyId === facultyId)
      return faculty ? faculty.name : facultyId
    },
    /** 获取性别中文标签 */
    getGenderLabel(gender) {
      const map = { 'M': '男', 'F': '女', 'OTHER': '其他' }
      return map[gender] || '-'
    },
    /** 获取学籍状态中文标签 */
    getStudentStatusLabel(status) {
      const map = {
        'ENROLLED': '在读',
        'GRADUATED': '已毕业',
        'SUSPENDED': '休学'
      }
      return map[status] || status
    },
    /** 获取学籍状态标签颜色 */
    getStudentStatusTagType(status) {
      const map = {
        'ENROLLED': 'success',
        'GRADUATED': 'info',
        'SUSPENDED': 'warning'
      }
      return map[status] || ''
    }
  }
}
</script>

<style scoped>
</style>

<style>
/* 指导老师下拉框样式 - 确保层级足够高 */
.faculty-select-popper {
  z-index: 9999 !important;
}
</style>
