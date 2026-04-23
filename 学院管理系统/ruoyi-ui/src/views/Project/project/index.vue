<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目编号" prop="projectCode">
        <el-input
          v-model="queryParams.projectCode"
          placeholder="请输入项目编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input
          v-model="queryParams.projectName"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始日期" prop="startDate">
        <el-date-picker clearable
          v-model="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择开始日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期" prop="endDate">
        <el-date-picker clearable
          v-model="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="负责人" prop="leaderName">
        <el-select
          v-model="queryParams.leaderId"
          placeholder="请输入负责人名称搜索"
          clearable
          filterable
          remote
          :remote-method="remoteSearchLeader"
          @change="handleQuery"
          style="width: 200px"
        >
          <el-option
            v-for="item in leaderOptions"
            :key="item.userId"
            :label="item.name + ' (' + (item.userType === 'FACULTY' ? '教师' : '学生') + ')'"
            :value="item.userId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="项目级别" prop="projectLevel">
        <el-select
          v-model="queryParams.projectLevel"
          placeholder="请选择项目级别"
          clearable
          @change="handleQuery"
          style="width: 150px"
        >
          <el-option label="国家级" value="NATIONAL" />
          <el-option label="省级" value="PROVINCIAL" />
          <el-option label="校级" value="UNIVERSITY" />
          <el-option label="企业" value="ENTERPRISE" />
          <el-option label="其他" value="OTHER" />
        </el-select>
      </el-form-item>
      <el-form-item label="项目状态" prop="projectStatus">
        <el-select
          v-model="queryParams.projectStatus"
          placeholder="请选择项目状态"
          clearable
          @change="handleQuery"
          style="width: 150px"
        >
          <el-option label="申报中" value="PROPOSED" />
          <el-option label="进行中" value="ONGOING" />
          <el-option label="已完成" value="COMPLETED" />
          <el-option label="暂停" value="SUSPENDED" />
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
          v-hasPermi="['Project:project:add']"
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
          v-hasPermi="['Project:project:edit']"
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
          v-hasPermi="['Project:project:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['Project:project:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="projectList" @selection-change="handleSelectionChange" @expand-change="handleExpandChange" :row-key="getRowKey">
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div style="padding: 10px 20px;">
            <!-- 项目成员 -->
            <div style="margin-bottom: 15px;">
              <div style="margin-bottom: 8px; font-weight: bold; color: #67C23A;">
                <i class="el-icon-user"></i> 项目成员 ({{ (scope.row.projectMemberList || []).length }} 人)
              </div>
              <el-table :data="scope.row.projectMemberList || []" size="mini" border v-if="(scope.row.projectMemberList || []).length > 0">
                <el-table-column type="index" label="序号" width="50" align="center" />
                <el-table-column label="成员姓名" min-width="120" align="center">
                  <template slot-scope="memberScope">
                    <span>{{ memberScope.row.memberName }}</span>
                    <el-tag size="mini" :type="memberScope.row.memberType === 'FACULTY' ? 'primary' : 'success'" style="margin-left: 5px;">
                      {{ memberScope.row.memberType === 'FACULTY' ? '教师' : '学生' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="角色" width="120" align="center">
                  <template slot-scope="memberScope">
                    <el-tag :type="getRoleTypeTagType(memberScope.row.roleType)" size="small">
                      {{ getRoleTypeLabel(memberScope.row.roleType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="角色描述" prop="roleDescription" min-width="150" show-overflow-tooltip />
                <el-table-column label="加入日期" width="100" align="center">
                  <template slot-scope="memberScope">
                    <span>{{ parseTime(memberScope.row.joinDate, '{y}-{m}-{d}') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="贡献比例" width="90" align="center">
                  <template slot-scope="memberScope">
                    <span v-if="memberScope.row.contributionPercent">{{ memberScope.row.contributionPercent }}%</span>
                    <span v-else>-</span>
                  </template>
                </el-table-column>
              </el-table>
              <div v-else style="color: #909399; font-size: 12px; padding: 5px 0;">暂无成员</div>
            </div>

            <!-- 项目成果 -->
            <div style="margin-bottom: 15px;">
              <div style="margin-bottom: 8px; font-weight: bold; color: #409EFF;">
                <i class="el-icon-document-checked"></i> 项目成果 ({{ (scope.row.projectOutputList || []).length }} 条)
              </div>
              <el-table :data="scope.row.projectOutputList || []" size="mini" border v-if="(scope.row.projectOutputList || []).length > 0">
                <el-table-column type="index" label="序号" width="60" align="center" />
                <el-table-column label="成果类型" width="100" align="center">
                  <template slot-scope="outputScope">
                    <el-tag :type="outputScope.row.outputType === 'PAPER' ? 'success' : 'warning'" size="small">
                      {{ outputScope.row.outputType === 'PAPER' ? '论文' : '专利' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="成果名称" min-width="200" show-overflow-tooltip>
                  <template slot-scope="outputScope">
                    <span>{{ getOutputName(outputScope.row) }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="产出日期" width="120" align="center">
                  <template slot-scope="outputScope">
                    <span>{{ parseTime(outputScope.row.outputDate, '{y}-{m}-{d}') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="贡献描述" min-width="150" show-overflow-tooltip prop="contributionDescription" />
              </el-table>
              <div v-else style="color: #909399; font-size: 12px; padding: 5px 0;">暂无成果</div>
            </div>

            <!-- 项目资金记录 -->
            <div>
              <div style="margin-bottom: 8px; font-weight: bold; color: #E6A23C;">
                <i class="el-icon-money"></i> 项目资金记录 ({{ (scope.row.fundingRecordList || []).length }} 笔)
              </div>
              <el-table :data="scope.row.fundingRecordList || []" size="mini" border v-if="(scope.row.fundingRecordList || []).length > 0">
                <el-table-column type="index" label="序号" width="50" align="center" />
                <el-table-column label="金额" width="120" align="center">
                  <template slot-scope="fundingScope">
                    <span style="color: #F56C6C; font-weight: bold;">¥{{ fundingScope.row.amount }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="拨款日期" width="120" align="center">
                  <template slot-scope="fundingScope">
                    <span>{{ parseTime(fundingScope.row.allocationDate, '{y}-{m}-{d}') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="资金来源" prop="fundingSource" min-width="150" show-overflow-tooltip />
                <el-table-column label="资金类型" width="100" align="center">
                  <template slot-scope="fundingScope">
                    <el-tag size="small" :type="getFundingTypeTagType(fundingScope.row.fundingType)">
                      {{ getFundingTypeLabel(fundingScope.row.fundingType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="状态" width="100" align="center">
                  <template slot-scope="fundingScope">
                    <el-tag size="small" :type="getFundingStatusTagType(fundingScope.row.status)">
                      {{ getFundingStatusLabel(fundingScope.row.status) }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
              <div v-else style="color: #909399; font-size: 12px; padding: 5px 0;">暂无资金记录</div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="项目ID" align="center" prop="projectId" />
      <el-table-column label="项目编号" align="center" prop="projectCode" />
      <el-table-column label="项目名称" align="center" prop="projectName" />
      <el-table-column label="项目描述" align="center" prop="description" />
      <el-table-column label="开始日期" align="center" prop="startDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束日期" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="项目级别" align="center" prop="projectLevel" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="getProjectLevelTagType(scope.row.projectLevel)">
            {{ getProjectLevelLabel(scope.row.projectLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="项目状态" align="center" prop="projectStatus" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="getProjectStatusTagType(scope.row.projectStatus)">
            {{ getProjectStatusLabel(scope.row.projectStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="负责人" align="center" min-width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.leaderName }} ({{ scope.row.leaderType === 'FACULTY' ? '教师' : '学生' }})</span>
        </template>
      </el-table-column>
      <el-table-column label="所属院系" align="center" prop="departmentName" min-width="120" />
      <el-table-column v-if="checkPermi(['Project:project:remove'])" label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['Project:project:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['Project:project:remove']"
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

    <!-- 添加或修改学院承担的科研项目信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="项目编号" prop="projectCode">
          <el-input v-model="form.projectCode" placeholder="请输入项目编号" />
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker clearable
            v-model="form.startDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择开始日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker clearable
            v-model="form.endDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="项目级别" prop="projectLevel">
          <el-select v-model="form.projectLevel" placeholder="请选择项目级别" style="width: 100%">
            <el-option label="国家级" value="NATIONAL" />
            <el-option label="省级" value="PROVINCIAL" />
            <el-option label="校级" value="UNIVERSITY" />
            <el-option label="企业" value="ENTERPRISE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目状态" prop="projectStatus">
          <el-select v-model="form.projectStatus" placeholder="请选择项目状态" style="width: 100%">
            <el-option label="申报中" value="PROPOSED" />
            <el-option label="进行中" value="ONGOING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="暂停" value="SUSPENDED" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人类型" prop="leaderType">
          <el-select v-model="form.leaderType" placeholder="请选择负责人类型" @change="handleLeaderTypeChange" style="width: 100%">
            <el-option label="教师" value="FACULTY" />
            <el-option label="学生" value="STUDENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="leaderId">
          <el-select
            v-model="form.leaderId"
            placeholder="请选择负责人"
            clearable
            filterable
            :disabled="!form.leaderType"
            @change="handleLeaderChange"
            style="width: 100%"
          >
            <el-option
              v-for="item in leaderSelectOptions"
              :key="item.userId"
              :label="item.name"
              :value="item.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属院系" prop="departmentName">
          <el-input v-model="form.departmentName" placeholder="请选择负责人后自动填充" disabled />
        </el-form-item>
        <el-divider content-position="center">项目成果关联（论文和专利）信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddProjectOutput">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteProjectOutput">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="projectOutputList" :row-class-name="rowProjectOutputIndex" @selection-change="handleProjectOutputSelectionChange" ref="projectOutput">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="成果类型" prop="outputType" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.outputType" placeholder="请选择成果类型" @change="handleOutputTypeChange(scope.row)" style="width: 100%">
                <el-option label="论文" value="PAPER" />
                <el-option label="专利" value="PATENT" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="成果名称" prop="outputName" min-width="200">
            <template slot-scope="scope">
              <el-select
                v-model="scope.row.outputId"
                placeholder="请输入成果名称搜索"
                clearable
                filterable
                remote
                :remote-method="(query) => remoteSearchOutput(query, scope.row)"
                :disabled="!scope.row.outputType"
                style="width: 100%"
              >
                <el-option
                  v-for="item in scope.row.outputOptions || []"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="产出日期" prop="outputDate" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.outputDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择产出日期" />
            </template>
          </el-table-column>
          <el-table-column label="贡献描述" prop="contributionDescription" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.contributionDescription" placeholder="请输入贡献描述" />
            </template>
          </el-table-column>
        </el-table>

        <el-divider content-position="center">项目成员信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddProjectMember">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteProjectMember">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="projectMemberList" :row-class-name="rowProjectMemberIndex" @selection-change="handleProjectMemberSelectionChange" ref="projectMember">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="成员类型" prop="memberType" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.memberType" placeholder="请选择成员类型" @change="handleMemberTypeChange(scope.row)" style="width: 100%">
                <el-option label="教师" value="FACULTY" />
                <el-option label="学生" value="STUDENT" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="成员姓名" prop="memberName" min-width="150">
            <template slot-scope="scope">
              <el-select
                v-model="scope.row.userId"
                placeholder="请输入成员姓名搜索"
                clearable
                filterable
                remote
                :remote-method="(query) => remoteSearchMember(query, scope.row)"
                :disabled="!scope.row.memberType"
                style="width: 100%"
              >
                <el-option
                  v-for="item in scope.row.memberOptions || []"
                  :key="item.userId"
                  :label="item.name"
                  :value="item.userId"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="角色" prop="roleType" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.roleType" placeholder="请选择角色" style="width: 100%">
                <el-option label="负责人" value="LEADER" />
                <el-option label="参与者" value="PARTICIPANT" />
                <el-option label="指导老师" value="ADVISOR" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="角色描述" prop="roleDescription" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.roleDescription" placeholder="请输入角色描述" />
            </template>
          </el-table-column>
          <el-table-column label="加入日期" prop="joinDate" width="180">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.joinDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择加入日期" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column label="贡献比例(%)" prop="contributionPercent" width="100">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.contributionPercent" :min="0" :max="100" :precision="2" controls-position="right" style="width: 100%" />
            </template>
          </el-table-column>
        </el-table>

        <el-divider content-position="center">项目资金记录信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddFundingRecord">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteFundingRecord">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="fundingRecordList" :row-class-name="rowFundingRecordIndex" @selection-change="handleFundingRecordSelectionChange" ref="fundingRecord">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="金额" prop="amount" width="150">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.amount" :precision="2" :min="0" controls-position="right" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column label="拨款日期" prop="allocationDate" width="180">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.allocationDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择拨款日期" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column label="资金来源" prop="fundingSource" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.fundingSource" placeholder="请输入资金来源" />
            </template>
          </el-table-column>
          <el-table-column label="资金类型" prop="fundingType" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.fundingType" placeholder="请选择资金类型" style="width: 100%">
                <el-option label="拨款" value="GRANT" />
                <el-option label="赞助" value="SPONSORSHIP" />
                <el-option label="配套" value="MATCHING" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="status" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="待处理" value="PENDING" />
                <el-option label="已到账" value="RECEIVED" />
                <el-option label="已支出" value="SPENT" />
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
import { listProject, getProject, delProject, addProject, updateProject } from "@/api/Project/project"
import { listFaculty } from "@/api/Faculty/faculty"
import { listStudent } from "@/api/Stu/student"
import { listPaper } from "@/api/Paper/Paper"
import { checkPermi } from "@/utils/permission"

export default {
  name: "Project",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedProjectOutput: [],
      // 项目成员表格数据
      projectMemberList: [],
      // 项目成员选中数据
      checkedProjectMember: [],
      // 项目资金记录表格数据
      fundingRecordList: [],
      // 项目资金记录选中数据
      checkedFundingRecord: [],
      // 负责人搜索选项
      leaderOptions: [],
      // 负责人选择选项
      leaderSelectOptions: [],
      // 预加载的教师列表（用于负责人和成员选择）
      allFacultyList: [],
      // 预加载的学生列表（用于负责人和成员选择）
      allStudentList: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 学院承担的科研项目信息表格数据
      projectList: [],
      // 项目成果关联（论文和专利）表格数据
      projectOutputList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectCode: null,
        projectName: null,
        startDate: null,
        endDate: null,
        projectLevel: null,
        projectStatus: null,
        leaderId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        projectCode: [
          { required: true, message: "项目编号不能为空", trigger: "blur" }
        ],
        projectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ],
        leaderType: [
          { required: true, message: "负责人类型：教师或学生不能为空", trigger: "change" }
        ],
        leaderId: [
          { required: true, message: "负责人ID不能为空", trigger: "blur" }
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
    /** 远程搜索负责人 */
    remoteSearchLeader(query) {
      if (query.length < 1) {
        this.leaderOptions = []
        return
      }
      Promise.all([
        listFaculty({ pageNum: 1, pageSize: 10, name: query }),
        listStudent({ pageNum: 1, pageSize: 10, name: query })
      ]).then(([facultyRes, studentRes]) => {
        const facultyList = (facultyRes.rows || []).map(item => ({
          userId: item.facultyId,
          name: item.name,
          userType: 'FACULTY'
        }))
        const studentList = (studentRes.rows || []).map(item => ({
          userId: item.studentId,
          name: item.name,
          userType: 'STUDENT'
        }))
        this.leaderOptions = [...facultyList, ...studentList]
      })
    },
    /** 负责人类型变更 */
    handleLeaderTypeChange() {
      this.form.leaderId = null
      this.form.departmentId = null
      this.form.departmentName = null
      // 使用预加载的数据，不再请求API
      if (this.form.leaderType === 'FACULTY') {
        this.leaderSelectOptions = this.allFacultyList || []
      } else if (this.form.leaderType === 'STUDENT') {
        this.leaderSelectOptions = this.allStudentList || []
      } else {
        this.leaderSelectOptions = []
      }
    },
    /** 负责人变更 */
    handleLeaderChange(leaderId) {
      const selectedLeader = this.leaderSelectOptions.find(item => item.userId === leaderId)
      if (selectedLeader) {
        this.form.departmentId = selectedLeader.departmentId
        this.form.departmentName = selectedLeader.departmentName
      } else {
        this.form.departmentId = null
        this.form.departmentName = null
      }
    },
    /** 成果类型变更 */
    handleOutputTypeChange(row) {
      row.outputId = null
      this.$set(row, 'outputOptions', [])
    },
    /** 远程搜索成果 */
    remoteSearchOutput(query, row) {
      if (query.length < 1) {
        this.$set(row, 'outputOptions', [])
        return
      }
      if (row.outputType === 'PAPER') {
        listPaper({ pageNum: 1, pageSize: 10, paperTitle: query }).then(res => {
          const options = (res.rows || []).map(item => ({
            id: item.paperId,
            name: item.paperTitle
          }))
          this.$set(row, 'outputOptions', options)
        }).catch(err => {
          console.error('搜索论文失败:', err)
          this.$set(row, 'outputOptions', [])
        })
      } else if (row.outputType === 'PATENT') {
        this.$message.warning('专利模块暂未实现')
        this.$set(row, 'outputOptions', [])
      }
    },
    /** 查询学院承担的科研项目信息列表 */
    getList() {
      this.loading = true
      listProject(this.queryParams).then(response => {
        this.projectList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 预加载教师列表 */
    loadFacultyList() {
      listFaculty({ pageNum: 1, pageSize: 9999 }).then(response => {
        this.allFacultyList = (response.rows || []).map(item => ({
          userId: item.facultyId,
          name: item.name,
          departmentId: item.departmentId,
          departmentName: item.departmentName
        }))
      })
    },
    /** 预加载学生列表 */
    loadStudentList() {
      listStudent({ pageNum: 1, pageSize: 9999 }).then(response => {
        this.allStudentList = (response.rows || []).map(item => ({
          userId: item.studentId,
          name: item.name,
          departmentId: item.departmentId,
          departmentName: item.departmentName
        }))
      })
    },
    /** 获取项目级别标签 */
    getProjectLevelLabel(level) {
      const levelMap = {
        'NATIONAL': '国家级',
        'PROVINCIAL': '省级',
        'UNIVERSITY': '校级',
        'ENTERPRISE': '企业',
        'OTHER': '其他'
      }
      return levelMap[level] || level
    },
    /** 获取项目级别标签样式 */
    getProjectLevelTagType(level) {
      const typeMap = {
        'NATIONAL': 'danger',
        'PROVINCIAL': 'warning',
        'UNIVERSITY': 'primary',
        'ENTERPRISE': 'success',
        'OTHER': 'info'
      }
      return typeMap[level] || ''
    },
    /** 获取项目状态标签 */
    getProjectStatusLabel(status) {
      const statusMap = {
        'PROPOSED': '申报中',
        'ONGOING': '进行中',
        'COMPLETED': '已完成',
        'SUSPENDED': '暂停'
      }
      return statusMap[status] || status
    },
    /** 获取项目状态标签样式 */
    getProjectStatusTagType(status) {
      const typeMap = {
        'PROPOSED': 'info',
        'ONGOING': 'primary',
        'COMPLETED': 'success',
        'SUSPENDED': 'warning'
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
        projectId: null,
        projectCode: null,
        projectName: null,
        description: null,
        startDate: null,
        endDate: null,
        projectLevel: null,
        projectStatus: null,
        leaderType: null,
        leaderId: null,
        departmentId: null,
        departmentName: null
      }
      this.projectOutputList = []
      this.projectMemberList = []
      this.fundingRecordList = []
      this.leaderSelectOptions = []
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
      this.ids = selection.map(item => item.projectId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加学院承担的科研项目信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const projectId = row.projectId || this.ids
      getProject(projectId).then(response => {
        this.form = response.data
        this.projectOutputList = response.data.projectOutputList
        this.projectMemberList = response.data.projectMemberList
        this.fundingRecordList = response.data.fundingRecordList
        // 根据负责人类型填充下拉选项，不调用 handleLeaderTypeChange（会清空 leaderId）
        if (this.form.leaderType === 'FACULTY') {
          this.leaderSelectOptions = this.allFacultyList || []
        } else if (this.form.leaderType === 'STUDENT') {
          this.leaderSelectOptions = this.allStudentList || []
        }
        this.open = true
        this.title = "修改学院承担的科研项目信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.projectOutputList = this.projectOutputList
          this.form.projectMemberList = this.projectMemberList
          this.form.fundingRecordList = this.fundingRecordList
          if (this.form.projectId != null) {
            updateProject(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addProject(this.form).then(response => {
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
      const projectIds = row.projectId || this.ids
      this.$modal.confirm('是否确认删除学院承担的科研项目信息编号为"' + projectIds + '"的数据项？').then(function() {
        return delProject(projectIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
	/** 项目成果关联（论文和专利）序号 */
    rowProjectOutputIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 获取行唯一键 */
    getRowKey(row) {
      return row.projectId
    },
    /** 展开行变化处理 */
    handleExpandChange(row, expandedRows) {
      if (expandedRows.includes(row)) {
        // 展开时加载成果和成员数据
        this.loadProjectDetails(row)
      }
    },
    /** 加载项目详情数据（成果和成员） */
    loadProjectDetails(row) {
      if (!row.projectOutputList || !row.projectMemberList || !row.fundingRecordList) {
        getProject(row.projectId).then(response => {
          this.$set(row, 'projectOutputList', response.data.projectOutputList || [])
          this.$set(row, 'projectMemberList', response.data.projectMemberList || [])
          this.$set(row, 'fundingRecordList', response.data.fundingRecordList || [])
        })
      }
    },
    /** 获取成果名称 */
    getOutputName(output) {
      if (output.outputType === 'PAPER') {
        return output.paperTitle || '未知论文'
      } else if (output.outputType === 'PATENT') {
        return output.patentName || '未知专利'
      }
      return '未知成果'
    },
    /** 获取角色类型标签 */
    getRoleTypeLabel(roleType) {
      const roleTypeMap = {
        'LEADER': '负责人',
        'PARTICIPANT': '参与者',
        'ADVISOR': '指导老师',
        'OTHER': '其他'
      }
      return roleTypeMap[roleType] || roleType
    },
    /** 获取角色类型标签样式 */
    getRoleTypeTagType(roleType) {
      const tagTypeMap = {
        'LEADER': 'danger',
        'PARTICIPANT': 'primary',
        'ADVISOR': 'warning',
        'OTHER': 'info'
      }
      return tagTypeMap[roleType] || ''
    },
    /** 获取资金类型标签 */
    getFundingTypeLabel(fundingType) {
      const fundingTypeMap = {
        'GRANT': '拨款',
        'SPONSORSHIP': '赞助',
        'MATCHING': '配套',
        'OTHER': '其他'
      }
      return fundingTypeMap[fundingType] || fundingType
    },
    /** 获取资金类型标签样式 */
    getFundingTypeTagType(fundingType) {
      const tagTypeMap = {
        'GRANT': 'success',
        'SPONSORSHIP': 'warning',
        'MATCHING': 'primary',
        'OTHER': 'info'
      }
      return tagTypeMap[fundingType] || 'info'
    },
    /** 获取资金状态标签 */
    getFundingStatusLabel(status) {
      const statusMap = {
        'PENDING': '待处理',
        'RECEIVED': '已到账',
        'SPENT': '已支出'
      }
      return statusMap[status] || status
    },
    /** 获取资金状态标签样式 */
    getFundingStatusTagType(status) {
      const tagTypeMap = {
        'PENDING': 'info',
        'RECEIVED': 'success',
        'SPENT': 'danger'
      }
      return tagTypeMap[status] || 'info'
    },
    /** 项目成果关联（论文和专利）添加按钮操作 */
    handleAddProjectOutput() {
      let obj = {}
      obj.outputType = ""
      obj.outputId = ""
      obj.outputDate = ""
      obj.contributionDescription = ""
      obj.outputOptions = []
      this.projectOutputList.push(obj)
      // 强制更新视图
      this.$set(this.projectOutputList, this.projectOutputList.length - 1, obj)
    },
    /** 项目成果关联（论文和专利）删除按钮操作 */
    handleDeleteProjectOutput() {
      if (this.checkedProjectOutput.length == 0) {
        this.$modal.msgError("请先选择要删除的项目成果关联（论文和专利）数据")
      } else {
        const projectOutputList = this.projectOutputList
        const checkedProjectOutput = this.checkedProjectOutput
        this.projectOutputList = projectOutputList.filter(function(item) {
          return checkedProjectOutput.indexOf(item.index) == -1
        })
      }
    },
    /** 复选框选中数据 */
    handleProjectOutputSelectionChange(selection) {
      this.checkedProjectOutput = selection.map(item => item.index)
    },
    /** 项目成员序号 */
    rowProjectMemberIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 成员类型变更 */
    handleMemberTypeChange(row) {
      row.userId = null
      this.$set(row, 'memberOptions', [])
    },
    /** 远程搜索成员 */
    remoteSearchMember(query, row) {
      if (query.length < 1) {
        this.$set(row, 'memberOptions', [])
        return
      }
      if (row.memberType === 'FACULTY') {
        listFaculty({ pageNum: 1, pageSize: 10, name: query }).then(res => {
          const options = (res.rows || []).map(item => ({
            userId: item.facultyId,
            name: item.name
          }))
          this.$set(row, 'memberOptions', options)
        }).catch(err => {
          console.error('搜索教师失败:', err)
          this.$set(row, 'memberOptions', [])
        })
      } else if (row.memberType === 'STUDENT') {
        listStudent({ pageNum: 1, pageSize: 10, name: query }).then(res => {
          const options = (res.rows || []).map(item => ({
            userId: item.studentId,
            name: item.name
          }))
          this.$set(row, 'memberOptions', options)
        }).catch(err => {
          console.error('搜索学生失败:', err)
          this.$set(row, 'memberOptions', [])
        })
      }
    },
    /** 项目成员添加按钮操作 */
    handleAddProjectMember() {
      let obj = {}
      obj.memberType = ""
      obj.userId = null
      obj.roleType = "PARTICIPANT"
      obj.roleDescription = ""
      obj.joinDate = ""
      obj.contributionPercent = null
      obj.memberOptions = []
      this.projectMemberList.push(obj)
      // 强制更新视图
      this.$set(this.projectMemberList, this.projectMemberList.length - 1, obj)
    },
    /** 项目成员删除按钮操作 */
    handleDeleteProjectMember() {
      if (this.checkedProjectMember.length == 0) {
        this.$modal.msgError("请先选择要删除的项目成员数据")
      } else {
        const projectMemberList = this.projectMemberList
        const checkedProjectMember = this.checkedProjectMember
        this.projectMemberList = projectMemberList.filter(function(item) {
          return checkedProjectMember.indexOf(item.index) == -1
        })
      }
    },
    /** 项目成员复选框选中数据 */
    handleProjectMemberSelectionChange(selection) {
      this.checkedProjectMember = selection.map(item => item.index)
    },
    /** 项目资金记录序号 */
    rowFundingRecordIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 项目资金记录添加按钮操作 */
    handleAddFundingRecord() {
      let obj = {}
      obj.amount = null
      obj.allocationDate = null
      obj.fundingSource = null
      obj.fundingType = null
      obj.status = null
      this.fundingRecordList.push(obj)
      // 强制更新视图
      this.$set(this.fundingRecordList, this.fundingRecordList.length - 1, obj)
    },
    /** 项目资金记录删除按钮操作 */
    handleDeleteFundingRecord() {
      if (this.checkedFundingRecord.length == 0) {
        this.$modal.msgError("请先选择要删除的项目资金记录数据")
      } else {
        const fundingRecordList = this.fundingRecordList
        const checkedFundingRecord = this.checkedFundingRecord
        this.fundingRecordList = fundingRecordList.filter(function(item) {
          return checkedFundingRecord.indexOf(item.index) == -1
        })
      }
    },
    /** 项目资金记录复选框选中数据 */
    handleFundingRecordSelectionChange(selection) {
      this.checkedFundingRecord = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('Project/project/export', {
        ...this.queryParams
      }, `project_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
