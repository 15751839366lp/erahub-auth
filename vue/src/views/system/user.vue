<template>
    <div id="users">
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>系统管理</el-breadcrumb-item>
            <el-breadcrumb-item>用户管理</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 用户列表卡片区 -->
        <el-card class="box-card">
            <el-form :inline="true" ref="form" :model="queryMap" label-width="70px" size="small">
                <el-form-item label="部门">
                    <el-select
                            clearable
                            @change="searchUser"
                            @clear="searchUser"
                            v-model="queryMap.departmentId"
                            placeholder="请选择所属部门"
                    >
                        <el-option
                                v-for="department in departments"
                                :label="department.name"
                                :key="department.id"
                                :value="department.id"
                        >
                            <span style="float: left">{{ department.name }}</span>
                            <span style="float: right; color: #8492a6; font-size: 13px">
                <el-tag size="small" effect="plain" type="success">
                  {{ department.total }}人
                </el-tag>
              </span>
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="用户名">
                    <el-input
                            @keyup.enter.native="searchUser"
                            @clear="searchUser"
                            clearable
                            v-model="queryMap.username"
                            placeholder="请输入用户名查询"
                    ></el-input>
                </el-form-item>

                <el-form-item label="邮箱">
                    <el-input
                            @keyup.enter.native="searchUser"
                            clearable
                            @clear="searchUser"
                            v-model="queryMap.email"
                            placeholder="请输入邮箱查询"
                    ></el-input>
                </el-form-item>

                <el-form-item label="性别">
                    <el-radio v-model="queryMap.sex" label="1">男</el-radio>
                    <el-radio v-model="queryMap.sex" label="0">女</el-radio>
                    <el-radio v-model="queryMap.sex" label>全部</el-radio>
                </el-form-item>

                <el-form-item label="真实姓名">
                    <el-input clearable @clear="searchUser" v-model="queryMap.realname"
                              placeholder="请输入真实姓名查询"></el-input>
                </el-form-item>
                <!--                <el-form-item label="状态">-->
                <!--                  <el-select-->
                <!--                    clearable-->
                <!--                    v-model="queryMap.isban"-->
                <!--                    @clear="searchUser"-->
                <!--                    placeholder="请选择用户状态"-->
                <!--                  >-->
                <!--                    <el-option label="全部" value=""></el-option>-->
                <!--                    <el-option label="禁用" value="1"></el-option>-->
                <!--                    <el-option label="正常" value="0"></el-option>-->
                <!--                  </el-select>-->
                <!--                </el-form-item>-->

                <el-form-item style="margin-left:50px;">
                    <el-button @click="reset" icon="el-icon-refresh">重置</el-button>
                    <el-button type="primary" @click="searchUser" icon="el-icon-search">查询</el-button>
                    <el-button
                            type="success"
                            icon="el-icon-plus"
                            @click="openAddDialog"
                            v-hasPermission="'user:add'"
                    >添加
                    </el-button>
                    <el-button @click="downExcel" v-hasPermission="'user:export'" icon="el-icon-download">导出</el-button>
                </el-form-item>
            </el-form>

            <!-- 表格区域 -->
            <el-table v-loading="loading" size="small" :data="userList" border style="width: 100%;" height="450" >
                <!-- <el-table-column type="selection" width="40"></el-table-column> -->
                <el-table-column label="#" prop="id" width="50" fixed></el-table-column>
                <el-table-column prop="username" label="用户名" width="110" fixed></el-table-column>
                <el-table-column prop="sex" :formatter="showSex" label="性别" width="100">
                    <template #default="scope">
                        <el-tag size="small" type="success" v-if="scope.row.sex===1">男</el-tag>
                        <el-tag size="small" type="warning" v-else>女</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="departmentName" label="所属部门" width="150" sortable></el-table-column>
                <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
                <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
                <el-table-column prop="phoneNumber" label="电话" width="150"></el-table-column>
                <el-table-column prop="birth" label="生日" width="180" sortable></el-table-column>
                <el-table-column prop="isban" label="是否禁用" width="100" fixed="right">
                    <template #default="scope">
                        <el-switch v-model="scope.row.status" @change="changUserStatus(scope.row)"></el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="230">
                    <template #default="scope">
                        <el-tooltip
                                class="item"
                                effect="dark"
                                content="修改密码"
                                placement="top"
                                :enterable="false"

                        >
                            <el-button v-hasPermission="'user:edit'" size="small" type="primary" icon="el-icon-edit"
                                       @click="openChangeUserPasswordDialog(scope.row.id)"></el-button>
                        </el-tooltip>

                        <el-tooltip
                                class="item"
                                effect="dark"
                                content="修改信息"
                                placement="top"
                                :enterable="false"

                        >
                        <el-button v-hasPermission="'user:edit'" size="small" type="primary" icon="el-icon-edit-outline"
                                   @click="edit(scope.row.id)"></el-button>
                        </el-tooltip>

                        <el-tooltip
                                class="item"
                                effect="dark"
                                content="删除角色"
                                placement="top"
                                :enterable="false"

                        >
                        <el-button v-hasPermission="'user:delete'" type="danger" size="small" icon="el-icon-delete"
                                   @click="del(scope.row.id)"></el-button>
                        </el-tooltip>

                        <el-tooltip
                                class="item"
                                effect="dark"
                                content="分配角色"
                                placement="top"
                                :enterable="false"

                        >
                            <el-button
                                    type="warning"
                                    size="small"
                                    icon="el-icon-s-tools"
                                    @click="openAssignRoles(scope.row.id)"
                            ></el-button>
                        </el-tooltip>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页 -->
            <el-pagination
                    style="margin-top:10px;"
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryMap.pageNum"
                    :page-sizes="[5, 10, 30, 50]"
                    :page-size="queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>

            <!-- 添加对话框 -->
            <el-dialog title="添加用户" @close="closeDialog" v-model="addDialogVisible">
                        <!-- 表单 -->
                        <span>
                  <el-form
                          :model="addForm"
                          :label-position="labelPosition"
                          :rules="addFormRules"
                          ref="addFormRef"
                          label-width="80px"
                  >
                    <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="用户名" prop="username">
                            <el-input v-model="addForm.username"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="12">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="部门" prop="departmentId">
                            <el-select v-model="addForm.departmentId" placeholder="请选择所属部门">
                              <el-option
                                      v-for="department in departments"
                                      :key="department.id"
                                      :label="department.name"
                                      :value="department.id"
                              ></el-option>
                            </el-select>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="10">
                        <div class="grid-content bg-purple">
                          <el-form-item label="真实姓名" prop="realname">
                            <el-input v-model="addForm.realname"></el-input>
                          </el-form-item>
                        </div>
                      </el-col>
                      <el-col :span="12">
                        <div class="grid-content bg-purple-light">
                          <el-form-item label="性别" prop="sex">
                            <el-radio-group v-model="addForm.sex">
                              <el-radio :label="1">男</el-radio>
                              <el-radio :label="0">女</el-radio>
                            </el-radio-group>
                          </el-form-item>
                        </div>
                      </el-col>
                    </el-row>
                    <el-form-item label="密码" prop="password">
                      <el-input v-model="addForm.password"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email">
                      <el-input v-model="addForm.email"></el-input>
                    </el-form-item>
                    <el-form-item label="手机" prop="phoneNumber">
                      <el-input v-model="addForm.phoneNumber"></el-input>
                    </el-form-item>
                    <el-form-item prop="birth" label="生日">
                      <el-col :span="11">
                        <el-date-picker
                                type="date"
                                value-format="YYYY年MM月DD日"
                                placeholder="选择出生日期"
                                v-model="addForm.birth"
                                style="width: 100%;"
                        ></el-date-picker>
                      </el-col>
                    </el-form-item>
                  </el-form>
                </span>
                <template #footer>
                    <span class="dialog-footer">
                        <el-button @click="addDialogVisible = false">取 消</el-button>
                        <el-button
                                type="primary"
                                @click="addUser"
                                :loading="btnLoading"
                                :disabled="btnDisabled"
                        >确 定</el-button>
                    </span>
                </template>
            </el-dialog>

            <!-- 修改密码对话框 -->
            <el-dialog title="修改密码" v-model="changeUserPasswordDialogVisible" @close="changeUserPasswordClose">
                    <span>
                      <el-form
                              :model="changeUserPasswordForm"
                              :label-position="labelPosition"
                              :rules="addFormRules"
                              ref="changeUserPasswordFormRef"
                              label-width="80px"
                      >
                        <el-row>
                          <el-col :span="10">
                            <div class="grid-content bg-purple">
                              <el-form-item label="用户名" prop="username">
                                <el-input v-model="changeUserPasswordForm.username" :disabled="true"></el-input>
                                <el-input
                                        type="hidden"
                                        v-model="changeUserPasswordForm.id"
                                        :disabled="true"
                                        style="display:none;"
                                ></el-input>
                              </el-form-item>
                            </div>
                          </el-col>
                        </el-row>
                        <el-row>
                          <el-col :span="10">
                            <div class="grid-content bg-purple">
                              <el-form-item label="密码" prop="password">
                                <el-input v-model="changeUserPasswordForm.password"></el-input>
                              </el-form-item>
                            </div>
                          </el-col>
                        </el-row>
                        <el-row>
                          <el-col :span="10">
                            <div class="grid-content bg-purple">
                              <el-form-item label="确认密码" prop="rePassword">
                                <el-input v-model="changeUserPasswordForm.rePassword"></el-input>
                              </el-form-item>
                            </div>
                          </el-col>
                        </el-row>
                      </el-form>
                    </span>
                <template #footer>
                            <span class="dialog-footer">
                      <el-button @click="changeUserPasswordDialogVisible = false">取 消</el-button>
                      <el-button
                              type="primary"
                              @click="changePassword"
                              :loading="btnLoading"
                              :disabled="btnDisabled"
                      >确 定</el-button>
                    </span>
                </template>
            </el-dialog>

            <!-- 修改对话框 -->
            <el-dialog title="修改用户" v-model="editDialogVisible" @close="editClose">
                    <span>
                      <el-form
                              :model="editForm"
                              :label-position="labelPosition"
                              :rules="addFormRules"
                              ref="editFormRef"
                              label-width="80px"
                      >
                        <el-row>
                          <el-col :span="10">
                            <div class="grid-content bg-purple">
                              <el-form-item label="用户名" prop="username">
                                <el-input v-model="editForm.username" :disabled="true"></el-input>
                                <el-input
                                        type="hidden"
                                        v-model="editForm.id"
                                        :disabled="true"
                                        style="display:none;"
                                ></el-input>
                              </el-form-item>
                            </div>
                          </el-col>
                          <el-col :span="12">
                            <div class="grid-content bg-purple-light">
                              <el-form-item label="部门" prop="departmentId">
                                <el-select v-model="editForm.departmentId" placeholder="请选择所属部门">
                                  <el-option
                                          v-for="department in departments"
                                          :key="department.id"
                                          :label="department.name"
                                          :value="department.id"
                                  ></el-option>
                                </el-select>
                              </el-form-item>
                            </div>
                          </el-col>
                        </el-row>

                        <el-row>
                          <el-col :span="10">
                            <div class="grid-content bg-purple">
                              <el-form-item label="真实姓名" prop="realname">
                                <el-input v-model="editForm.realname"></el-input>
                              </el-form-item>
                            </div>
                          </el-col>
                          <el-col :span="12">
                            <div class="grid-content bg-purple-light">
                              <el-form-item label="性别" prop="sex">
                                <el-radio-group v-model="editForm.sex">
                                  <el-radio :label="1">男</el-radio>
                                  <el-radio :label="0">女</el-radio>
                                </el-radio-group>
                              </el-form-item>
                            </div>
                          </el-col>
                        </el-row>

                        <el-form-item label="邮箱" prop="email">
                          <el-input v-model="editForm.email"></el-input>
                        </el-form-item>
                        <el-form-item label="联系方式" prop="phoneNumber">
                          <el-input v-model="editForm.phoneNumber"></el-input>
                        </el-form-item>
                        <el-form-item prop="birth" label="生日">
                          <el-col :span="11">
                            <el-date-picker
                                    type="date"
                                    value-format="YYYY年MM月DD日"
                                    placeholder="选择出生日期"
                                    v-model="editForm.birth"
                                    style="width: 100%;"
                            ></el-date-picker>
                          </el-col>
                        </el-form-item>
                      </el-form>
                    </span>
                            <template #footer>
                            <span class="dialog-footer">
                      <el-button @click="editDialogVisible = false">取 消</el-button>
                      <el-button
                              type="primary"
                              @click="updateUser"
                              :loading="btnLoading"
                              :disabled="btnDisabled"
                      >确 定</el-button>
                    </span>
                            </template>
            </el-dialog>
            <!-- 分配角色对话框 -->
            <el-dialog center title="分配角色" v-model="assignDialogVisible">
        <span>
            <el-transfer
                    filterable
                    :titles="['未拥有','已拥有']"
                    :button-texts="['到左边', '到右边']"
                    v-model="value"
                    :data="roles"
                    style="text-align: left; display: inline-block"
            ></el-transfer>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="assignDialogVisible = false" class="el-icon-close">取消分配</el-button>
          <el-button
                  v-hasPermission="'user:assign'"
                  type="primary"
                  @click="doAssignRoles"
                  class="el-icon-check"
                  :loading="btnLoading"
                  :disabled="btnDisabled"
          >确定分配</el-button>
        </span>
                </template>
            </el-dialog>
        </el-card>
    </div>
</template>
<script>

    import {ref, reactive } from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import {
        getRoles,
        assignRoles,
        findUserList,
        deleteUser,
        add,
        changeUserPassword,
        update,
        editUser,
        updateStatus,
        findAll,
        excel
    } from '../../api/system/user'

    export default {
        setup() {
            let btnLoading = ref(false)
            let btnDisabled = ref(false)
            let departments = ref([])
            let loading = ref(true)
            let total = ref(0)
            let addDialogVisible = ref(false) //添加对话框
            let changeUserPasswordDialogVisible = ref(false) //修改密码对话框
            let editDialogVisible = ref(false) //修改对话框
            let assignDialogVisible = ref(false) //分配角色对话框
            let labelPosition = ref("right") //lable对齐方式
            //查询对象
            let queryMap = reactive({
                pageNum: 1,
                pageSize: 10,
                username: "",
                sex: "",
                realname: ""
            })
            let userList = ref([])

            let addForm = ref({
                username: "",
                realname: "",
                password: "",
                email: "",
                phoneNumber: "",
                sex: "",
                birth: ""
            }) //添加表单

            let changeUserPasswordForm = ref({
                id: "",
                username: "",
                password: "",
                rePassword: "",
            }) //修改密码表单

            let editForm = ref({
                id: "",
                departmentId: "",
                username: "",
                realname: "",
                password: "",
                email: "",
                phoneNumber: "",
                sex: "",
                birth: ""
            }) //更新表单
            const addFormRules = {
                username: [
                    {required: true, message: "请输入用户名", trigger: "blur"},
                    {min: 3, max: 12, message: "长度在 3 到 12 个字符", trigger: "blur"}
                ],
                password: [
                    {required: true, message: "请输入用户密码", trigger: "blur"},
                    {min: 6, max: 15, message: "长度在 6 到 15 个字符", trigger: "blur"}
                ],
                rePassword: [
                    {
                        required: true,
                        validator: (rule, value, callback) => {
                            if (value == "" || value == null) {
                                callback(new Error("请确认用户密码"));
                            }else if(value != changeUserPasswordForm.value.password){
                                callback(new Error("输入密码不一致密码"));
                            }else{
                                callback();
                            }
                        },
                        trigger: "blur"
                    },
                    {min: 6, max: 15, message: "长度在 6 到 15 个字符", trigger: "blur"}
                ],
                departmentId: [
                    {required: true, message: "请选择部门", trigger: "blur"}
                ],
                sex: [{required: true, message: "请选择性别", trigger: "blur"}],
                birth: [{required: false, message: "请填写出生日期", trigger: "blur"}],
                // email: [
                //     {
                //         required: false,
                //         validator: (rule, value, callback) => {
                //             const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
                //
                //             setTimeout(() => {
                //                 if (mailReg.test(value)) {
                //                     callback();
                //                 } else {
                //                     callback(new Error("请输入正确的邮箱格式"));
                //                 }
                //             }, 100);
                //         },
                //         trigger: "blur"}
                // ],
                // phoneNumber: [
                //     {
                //         required: false,
                //         message: "请输入联系方式",
                //         validator: (rule, value, callback) => {
                //             const phoneReg = /^1[34578]\d{9}$$/;
                //
                //             setTimeout(() => {
                //                 if (!Number.isInteger(+value)) {
                //                     callback(new Error("请输入数字值"));
                //                 } else {
                //                     if (phoneReg.test(value)) {
                //                         callback();
                //                     } else {
                //                         callback(new Error("电话号码格式不正确"));
                //                     }
                //                 }
                //             }, 100);
                //         },
                //         trigger: "blur"
                //     }
                // ],
                realname: [
                    {required: false, message: "请输入真实姓名", trigger: "blur"},
                    {min: 5, max: 10, message: "长度在 5 到 10 个字符", trigger: "blur"}
                ]
            } //添加表单验证规则
            let roles = ref([]) //角色
            let value = ref([]) //用户拥有的角色
            let uid = ref("")

            const addFormRef = ref(null);
            const changeUserPasswordFormRef = ref(null);
            const editFormRef = ref(null);

            /**
             * 重置
             */
            const reset = () => {
                queryMap.pageNum = 1
                queryMap.pageSize = 10
                queryMap.username = ""
                queryMap.sex = ""
                queryMap.realname = ""
                queryMap.email = ""
                queryMap.departmentId = null
                getUserList();
            }
            /**
             * 加载用户表格
             */
            const downExcel = () => {
                excel().then(res => {
                    if (res.headers["content-type"] === "application/json") {
                        return ElMessage.error(
                            "Subject does not have permission [user:export]"
                        );
                    }
                    const data = res.data;
                    let url = window.URL.createObjectURL(data); // 将二进制文件转化为可访问的url
                    const a = document.createElement("a");
                    document.body.appendChild(a);
                    a.href = url;
                    a.download = "用户列表.xlsx";
                    a.click();
                    window.URL.revokeObjectURL(url);
                });
            }
            /**
             * 弹出用户分配角色
             */
            const openAssignRoles = (id) => {
                const elLoading = ElLoading.service({
                    lock: true,
                    text: "Loading",
                    spinner: "el-icon-loading",
                    background: "rgba(0, 0, 0, 0.7)"
                });
                getRoles("/system/user/" + id + "/roles").then((res) => {
                    if (res.data.success) {
                        roles.value = res.data.data.roles;
                        value.value = res.data.data.values;
                        uid.value = id;
                        setTimeout(() => {
                            elLoading.close();
                            assignDialogVisible.value = true;
                        }, 400);
                    } else {
                        ElMessage.error("分配角色失败:" + res.data.data.errorMsg);
                    }
                }).catch((res) => {
                    ElMessage.error("分配角色失败:" + res);
                });

            }
            /**
             * 确定分配角色
             */
            const doAssignRoles = () => {
                assignDialogVisible.value = true;
                btnLoading.value = true;
                btnDisabled.value = true;
                assignRoles(
                    "system/user/" + uid.value + "/assignRoles",
                    value.value
                ).then((res) => {
                    if (res.data.success) {
                        ElNotification({
                            type: "success",
                            title: '操作成功',
                            message: '用户分配角色成功',
                        });
                    } else {
                        ElMessage.error("分配角色失败:" + res.data.data.errorMsg);
                    }
                    assignDialogVisible.value = false;
                    btnLoading.value = false;
                    btnDisabled.value = false;
                }).catch((res) => {
                    ElMessage.error("分配角色失败:" + res);
                    assignDialogVisible.value = false;
                    btnLoading.value = false;
                    btnDisabled.value = false;
                });

            }
            /**
             * 加载用户列表
             */
            const getUserList = () => {
                loading.value = true;
                findUserList(queryMap).then((res) => {
                    if (!res.data.success) {
                        loading.value = false;
                        return ElMessage.error("获取用户列表失败:" + res.data.data.errorMsg);
                    }
                    total.value = res.data.data.total;
                    userList.value = res.data.data.rows;
                    loading.value = false;
                }).catch((res) => {
                    loading.value = false;
                    ElMessage.error("加载用户列表 失败:" + res);
                });
            }

            /**
             * 删除用户
             */
            const del = (id) => {
                ElMessageBox.confirm(
                    "此操作将永久删除该用户, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteUser("system/user/delete/" + id).then((res) => {
                            console.log(res);
                            if (res.data.success) {
                                ElNotification({
                                    type: "success",
                                    title: '操作成功',
                                    message: '用户删除成功',
                                });
                                getUserList();
                                getDepartmets();
                            } else {
                                ElMessage.error(res.data.data.errorMsg);
                            }
                        }).catch((res) => {
                            ElMessage.error("用户删除失败:" + res);
                        });
                    }
                }).catch(() => {
                    ElMessage({
                        type: "info",
                        message: "已取消删除"
                    });
                });
            }
            /**
             * 添加用户
             */
            const openAddDialog = () => {
                addDialogVisible.value = true
            }
            const addUser = () => {
                addFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;
                        add(addForm.value).then((res) => {
                            if (res.data.success) {
                                ElNotification({
                                    type: "success",
                                    title: '操作成功',
                                    message: '用户添加成功',
                                });
                                addFormRef.value.resetFields();
                                // Object.keys(addForm).forEach(key => delete addForm[key])
                                getUserList();
                                getDepartmets();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                addDialogVisible.value = false;
                            } else {
                                btnDisabled.value = false;
                                btnLoading.value = false;
                                return ElMessage.error("用户添加失败:" + res.data.data.errorMsg);
                            }
                        }).catch((res) => {
                            ElMessage.error("用户添加失败:" + res);
                            addFormRef.value.resetFields();
                            // Object.keys(addForm).forEach(key => delete addForm[key])
                            addDialogVisible.value = false;
                            btnDisabled.value = false;
                            btnLoading.value = false;
                        });
                    }
                });
            }
            /**
             * 修改密码
             */
            const changePassword = () => {
                changeUserPasswordFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;


                        ElMessageBox.confirm('确定修改用户密码吗？').then(() => {
                            changeUserPassword(changeUserPasswordForm.value).then((res) => {
                                if (res.data.success) {
                                    ElNotification({
                                        type: "success",
                                        title: '操作成功',
                                        message: '修改密码成功',
                                    });
                                    changeUserPasswordFormRef.value.resetFields();
                                    // Object.keys(addForm).forEach(key => delete addForm[key])
                                    getUserList();
                                    getDepartmets();
                                    btnLoading.value = false;
                                    btnDisabled.value = false;
                                    changeUserPasswordDialogVisible.value = false;
                                } else {
                                    btnDisabled.value = false;
                                    btnLoading.value = false;
                                    return ElMessage.error("修改密码失败:" + res.data.data.errorMsg);
                                }
                            }).catch((res) => {
                                ElMessage.error("修改密码失败:" + res);
                                changeUserPasswordFormRef.value.resetFields();
                                // Object.keys(addForm).forEach(key => delete addForm[key])
                                changeUserPasswordDialogVisible.value = false;
                                btnDisabled.value = false;
                                btnLoading.value = false;
                            });
                        }).catch(() => {

                        })
                    }
                });
            }
            /**
             * 更新用户
             */
            const updateUser = () => {
                editFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;

                        ElMessageBox.confirm('确定修改用户信息吗？').then(() => {
                            update(
                                "system/user/update/" + editForm.value.id,
                                editForm.value
                            ).then((res) => {
                                if (res.data.success) {
                                    ElNotification({
                                        title: "操作成功",
                                        message: "用户基本信息已更新",
                                        type: "success"
                                    });
                                    getUserList();
                                    getDepartmets();
                                    btnLoading.value = false;
                                    btnDisabled.value = false;
                                } else {
                                    ElMessage.error("用户信息更新失败:" + res.data.data.errorMsg);
                                }
                                editFormRef.value.resetFields();
                                editDialogVisible.value = false;
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            }).catch((res) => {
                                ElMessage.error("用户信息更新失败:" + res);
                                editFormRef.value.resetFields();
                                editDialogVisible.value = false;
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            });
                        }).catch(() => {

                        })
                    }
                });
            }
            /**
             * 搜索用户
             */
            const searchUser = () => {
                queryMap.pageNum = 1;
                getUserList();
            }

            /**
             * 修改密码
             */
            const openChangeUserPasswordDialog = (id) => {
                editUser("system/user/edit/" + id).then((res) => {
                    if (res.data.success) {
                        changeUserPasswordForm.value.id = res.data.data.id;
                        changeUserPasswordForm.value.username = res.data.data.username;
                        changeUserPasswordDialogVisible.value = true;
                    } else {
                        return ElMessage.error("用户密码修改失败:" + res.data.data.errorMsg);
                    }
                }).catch((res) => {
                    ElMessage.error("用户密码修改失败:" + res);
                });
            }

            /**
             * 修改用户信息
             */
            const edit = (id) => {
                editUser("system/user/edit/" + id).then((res) => {
                    if (res.data.success) {
                        editForm.value = res.data.data;
                        editDialogVisible.value = true;
                    } else {
                        return ElMessage.error("用户信息编辑失败:" + res.data.data.errorMsg);
                    }
                }).catch((res) => {
                    ElMessage.error("用户信息更新失败:" + res);
                });
            }
            /**
             *  改变页码
             */
            const handleSizeChange = (newSize) => {
                queryMap.pageSize = newSize;
                getUserList();
            }
            /**
             * 翻页
             */
            const handleCurrentChange = (current) => {
                queryMap.pageNum = current;
                getUserList();
            }
            /**
             * 关闭添加弹出框
             */
            const closeDialog = () => {
                addFormRef.value.clearValidate();
                addForm.value.birth = "";
                addForm.value = {};
            }
            /**
             * 关闭编辑弹出框
             */
            const editClose = () => {
                editFormRef.value.clearValidate();
                editForm.value = {};
            }
            /**
             * 关闭编辑弹出框
             */
            const changeUserPasswordClose = () => {
                changeUserPasswordFormRef.value.clearValidate();
                changeUserPasswordForm.value = {};
            }
            /**
             * 禁用启用用户
             */
            const changUserStatus = (row) => {
                updateStatus(
                    "system/user/updateStatus/" + row.id + "/" + row.status
                ).then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("更新用户状态失败:" + res.data.data.errorMsg);
                        row.status = !row.status;
                    } else {
                        const message = row.status ? '用户状态已禁用' : '用户状态已启用';
                        ElNotification({
                            type: 'success',
                            title: '操作成功',
                            message: message,
                        });
                    }
                }).catch((res) => {
                    ElMessage.error("更新用户状态失败:" + res);
                });
            }
            /**
             * 加载所有部门
             */
            const getDepartmets = () => {
                findAll().then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取部门列表失败:" + res.data.data.errorMsg);
                    }
                    departments.value = res.data.data;
                }).catch((res) => {
                    ElMessage.error("更新用户状态失败:" + res);
                });
            }
            /**
             * 显示用户性别
             */
            const showSex = (row, column) => {
                return row.sex === 1 ? "男" : "女";
            }

            getUserList();
            getDepartmets();

            return {
                btnLoading,
                btnDisabled,
                departments,
                loading,
                total,
                addDialogVisible,
                changeUserPasswordDialogVisible,
                editDialogVisible,
                assignDialogVisible,
                labelPosition,
                queryMap,
                userList,
                addForm,
                changeUserPasswordForm,
                editForm,
                addFormRules,
                roles,
                value,
                uid,
                addFormRef,
                changeUserPasswordFormRef,
                editFormRef,
                reset,
                downExcel,
                openAssignRoles,
                doAssignRoles,
                getUserList,
                del,
                openAddDialog,
                addUser,
                changePassword,
                updateUser,
                searchUser,
                openChangeUserPasswordDialog,
                edit,
                handleSizeChange,
                handleCurrentChange,
                closeDialog,
                changeUserPasswordClose,
                editClose,
                changUserStatus,
                getDepartmets,
                showSex
            }
        }
    };
</script>

<style>
    .el-notification__icon.el-icon-success{
        color: #67c23a;
    }
    .el-notification__icon.el-icon-warning{
        color: #e6a23c;
    }
    .el-notification__icon.el-icon-info{
        color: #909399;
    }
    .el-notification__icon.el-icon-error{
        color: #f56c6c;
    }
</style>
