<template>
    <div class="about">
        <v-header/>
        <v-sidebar/>
        <div class="content-box" :class="{ 'content-collapse': collapse }">
            <v-tags></v-tags>
            <div class="content">
                <router-view v-slot="{ Component }">
                    <transition name="move" mode="out-in">
                        <keep-alive :include="tagsList">
                            <component :is="Component"/>
                        </keep-alive>
                    </transition>
                </router-view>
                <!-- <el-backtop target=".content"></el-backtop> -->
            </div>
        </div>
    </div>
</template>
<script>
    import {ref, reactive, computed, onMounted} from "vue";
    import {useStore} from "vuex";
    import {useRouter, useRoute} from "vue-router";
    import {ElMessage, ElLoading, ElMessageBox} from "element-plus";
    import vHeader from "../components/Header.vue";
    import vSidebar from "../components/Sidebar.vue";
    import vTags from "../components/Tags.vue"; //引进菜单模板
    import {findMenu, info} from '../api/system/user'

    export default {
        components: {
            vHeader,
            vSidebar,
            vTags
        },
        setup() {
            const store = useStore();
            const router = useRouter();
            const route = useRoute();

            let loading = ref(true)
            let menuList = reactive({})
            let userInfo = reactive({})

            const tagsList = computed(() =>
                store.state.component.tagsList.map((item) => item.name)
            );

            const collapse = computed(() => store.state.component.collapse);

            /**
             获取用户信息
             */
            const getUserInfo = () => {
                info().then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("获取用户信息失败:" + res.data.data.errorMsg);
                        router.push("/login");
                    } else {
                        userInfo = res.data.data;
                        store.commit("login/setUserInfo", res.data.data);
                    }
                }).catch((res) => {
                    ElMessage.error("获取用户信息失败:" + res.data.data.errorMsg);
                    router.push("/login");
                });
            }

            onMounted(() => {
                getUserInfo();
            })

            // this.activePath(window.sessionStorage.getItem("activePath"));
            setTimeout(() => {
                loading.value = false;
            }, 500);

            return {
                loading,
                menuList,
                userInfo,
                tagsList,
                collapse,
                getUserInfo,
            }
        },
    };
</script>

<style>

    .el-main {
        background-color: #eaedf1;
    }

    .home-container {
        position: relative;
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
