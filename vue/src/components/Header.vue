<template>
    <!--    导航-->
    <div class="header">
            <div class="collapse-btn" @click="collapseChage">
                <i v-if="!isOpen" class="el-icon-s-fold"></i>
                <i v-else class="el-icon-s-unfold"></i>
            </div>
            <div class="logo">后台管理系统</div>
        <div class="method">
            <div class="userInfo" >{{userInfo.username}}</div>
            <el-dropdown style="float: right;margin-right: 30px">
                <div class="block">
                    <div class="user-avator">
                        <img src="../assets/test03.jpg"/>
                    </div>
                    <!--                    <el-avatar :size="50" :src="import('../assets/test03.jpg').default"-->
                    <!--                               style="cursor: pointer;"></el-avatar>-->
                </div>
                <template #dropdown>
                    <el-dropdown-menu trigger="click">
                        <el-dropdown-item>
                            <span type="danger" @click="toWelcome"><span class="el-icon-house"></span> &nbsp;系统首页</span>
                        </el-dropdown-item>
                        <el-dropdown-item>
                            <span type="danger" @click="getContact"><span class="el-icon-ship"></span> &nbsp;交流讨论</span>
                        </el-dropdown-item>
                        <el-dropdown-item>
                            <span type="danger" @click="logout(true)"><span class="el-icon-switch-button"></span> &nbsp;退出登入</span>
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </div>

    </div>
</template>
<script>
    import {reactive, computed} from "vue";
    import {useStore} from "vuex";
    import {useRouter} from "vue-router";
    import {ElMessage, ElMessageBox} from "element-plus";

    export default {
        setup() {
            const store = useStore();
            const router = useRouter();

            let isOpen = computed(() => store.state.component.collapse);
            let userInfo = computed(() => store.state.login.userInfo);
            /**
             *
             * 退出登入
             */
            const collapseChage = () => {
                store.commit("component/handleCollapse", !store.state.component.collapse);
            }
            const logout = (isClick) => {
                if (isClick) {
                    ElMessageBox.confirm("此操作将退出系统, 是否继续?", "提示", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }).then(() => {
                        store.commit("component/clearTags");
                        store.commit("login/clearUserInfo");
                        window.localStorage.clear();
                        window.sessionStorage.clear();
                        router.push("/login");
                    }).catch(() => {
                        ElMessage({
                            type: "info",
                            message: "已取消退出登入"
                        });
                    });
                } else {
                    store.commit("component/clearTags");
                    store.commit("login/clearUserInfo");
                    window.localStorage.clear();
                    window.sessionStorage.clear();
                    router.push("/login");
                }

            }
            /**
             * 去系统首页
             */
            const toWelcome = () => {
                router.push("/system/welcome");
            }

            /**
             * 点击交流
             */
            const getContact = () => {
                const w = window.open('about:blank');
                w.location.href = '';
            }

            // this.activePath(window.sessionStorage.getItem("activePath"));

            return {
                userInfo,
                isOpen,
                collapseChage,
                logout,
                toWelcome,
                getContact
            }
        },
    };
</script>
<style scoped>
    /* 为对应的路由跳转时设置动画效果 */

    .header{
        background-color: #001529;
        position: relative;
        box-sizing: border-box;
        justify-content: space-between;
        align-items: center;
        color: #fff;
        width: 100%;
        height: 70px;
        font-size: 19px;
    }


    .collapse-btn {
        float: left;
        padding: 0 21px;
        cursor: pointer;
        line-height: 70px;
    }

    .userInfo{
        float: left;
        line-height: 70px;
        margin-right: 30px
    }

    .logo {
        display: block;
        float: left;
        width: 250px;
        line-height: 70px;
    }

    .method{
        float: right;
    }

    .el-dropdown{
        float: right;
    }

    .user-avator {
        float: right;
        margin-top: 10px;
    }

    .user-avator img {
        cursor: pointer;
        width: 50px;
        height: 50px;
        border-radius: 50%;
    }
</style>
