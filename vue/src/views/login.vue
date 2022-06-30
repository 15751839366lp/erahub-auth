<template>
    <div id="login">
        <Particles></Particles>
        <div class="ms-login">
            <h1 class="ms-title"
                style="position: absolute;color: #fff;left: 50%;transform: translateX(-50%); top: -110px;">后台管理系统</h1>
            <el-form
                    :model="userLoginForm"
                    :rules="loginRules"
                    status-icon
                    ref="userLoginFormRef"
                    label-position="left"
                    label-width="0px"
                    class="demo-ruleForm login-page"
            >
                <h3 class="title" style="padding: 30px;font-size: 20px">系统登录</h3>
                <el-form-item prop="username">
                    <el-input
                            type="text"
                            @keyup.enter.native="handleSubmit"
                            v-model="userLoginForm.username"
                            auto-complete="off"
                            placeholder="用户名"
                            prefix-icon="iconfont el-icon-user"
                    ></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                            @keyup.enter.native="handleSubmit"
                            type="password"
                            v-model="userLoginForm.password"
                            auto-complete="off"
                            placeholder="密码"
                            prefix-icon="el-icon-suitcase-1"
                    ></el-input>
                </el-form-item>
                <div></div>

                <el-checkbox v-model="checked" class="rememberme">记住密码</el-checkbox>
                <el-form-item style="width:100%;">
                    <div style="float:right;">
                        <el-button
                                type="primary"
                                class="el-icon-mobile-phone"
                                @click="handleSubmit"
                                :loading="loadingFull"
                        >登录
                        </el-button>
                        <el-button class="el-icon-refresh" @click="resetForm">重置</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </div>

        <!-- 验证码 -->
        <Vcode
                :show="isShow"
                @success="success"
                @close="close"
                :canvasWidth="500"
                :canvasHeight="350"
        />
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {useStore} from "vuex";
    import {useRouter} from "vue-router";
    import Vcode from "vue3-puzzle-vcode"
    import Particles from '../components/particles/index.vue'
    import {ElMessage, ElLoading} from "element-plus";
    import {login, info} from '../api/system/user'

    export default {
        components: {
            Vcode,
            Particles
        },
        setup() {
            const store = useStore();
            const router = useRouter();
            const loadingFull = ref(false)

            let isShow = ref(false)
            let dialogVisible = false
            let imgCode = undefined
            //表单用户登入数据
            let userLoginForm = reactive({
                username: "",
                password: ""
            })
            let checked = ref(true)
            //验证规则
            const loginRules = {
                username: [
                    {required: true, message: "请输入用户名", trigger: "blur"},
                    {min: 3, max: 12, message: "长度在 3 到 12 个字符", trigger: "blur"}
                ],
                password: [
                    {required: true, message: "请输入用户密码", trigger: "blur"},
                    {min: 6, max: 15, message: "长度在 6 到 15 个字符", trigger: "blur"}
                ]
            }

            const userLoginFormRef = ref(null);
            //登入提交
            const handleSubmit = () => {
                userLoginFormRef.value.validate((valid) => {
                    if (!valid) {
                        return;
                    } else {
                        loadingFull.value = true
                        isShow.value = true;
                    }
                });
            }
            //重置表单
            const resetForm = () => {
                userLoginFormRef.value.resetFields();
            }
            //验证成功
            const success = () => {
                let loading = ElLoading.service({
                    text: 'Loading',
                });
                //发起登入请求
                login(userLoginForm).then((res) => {
                    if (res.data.success) {
                        ElMessage.success("欢迎你进入系统");
                        window.localStorage.setItem("token", res.data.data);
                        getUserInfo();
                    } else {
                        loadingFull.value = false
                        isShow.value = false;
                        ElMessage.error(res.data.data.errorMsg);
                    }
                    loading.close();
                }).catch((res) => {
                    loadingFull.value = false
                    ElMessage.error(res);
                });

            }

            /**
             获取用户信息
             */
            const getUserInfo = () => {
                info().then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取用户信息失败:" + res.data.data.errorMsg);
                    } else {
                        // userInfo = res.data.data;
                        store.commit("login/setUserInfo", res.data.data);
                        router.push("/home");
                    }
                }).catch((res) => {
                    ElMessage.error(res.data.data.errorMsg);
                });

            }

            const close = () => {
                loadingFull.value = false
                isShow.value = false;
            }

            return {
                router,
                isShow,
                dialogVisible,
                imgCode,
                userLoginForm,
                userLoginFormRef,
                checked,
                loginRules,
                loadingFull,
                handleSubmit,
                resetForm,
                success,
                getUserInfo,
                close,

            }
        },
    };
</script>

<style scoped>
    .login-container {
        width: 100%;
        height: 100%;

    }

    #login {
        position: relative;
    }

    .ms-title {
        z-index: 999;
    }

    .login-page {
        z-index: 999;
        position: relative;
        -webkit-border-radius: 5px;
        border-radius: 5px;
        margin: 250px auto;
        width: 370px;
        padding: 40px 35px 15px;
        background: rgba(255, 255, 255, 0.5);
        border: 1px solid #eaeaea;
    }

    label.el-checkbox.rememberme {
        margin: 0px 0px 15px;
        text-align: left;
    }

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


