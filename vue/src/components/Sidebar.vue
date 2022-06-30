<template>
    <div class="sidebar">
        <el-menu
                class="el-menu-vertical-demo"
                :collapse="isOpen"
                :router="true"
                active-text-color="#20a0ff"
                :default-active="activePath"
                background-color="#001529"
                :collapse-transition="true"
                text-color="rgba(255,255,255,0.7)"
                unique-opened
        >
            <template v-for="item in menuList">
                <el-submenu :disabled="item.disabled" :index="item.id+''"
                            v-if="item.children != null && item.children != undefined && item.children.length>0"
                            :key="item.id+''">
                    <template #title>
                        <i :class="item.icon"></i>
                        <span>{{ item.menuName}}</span>
                    </template>
                    <template v-for="subItem in item.children">
                        <el-submenu :disabled="subItem.disabled" :index="subItem.id+''"
                                    v-if="subItem.children != null && subItem.children != undefined && subItem.children.length>0"
                                    :key="subItem.id+''">
                            <template #title>
                                <i :class="subItem.icon"></i>
                                <span>{{ subItem.menuName}}</span>
                            </template>
                            <template v-for="threeItem in subItem.children">
                                <el-submenu :disabled="threeItem.disabled" :index="threeItem.id+''"
                                            v-if="threeItem.children != null && threeItem.children != undefined && threeItem.children.length>0" :key="threeItem.id+''">
                                    <template #title>
                                        <i :class="threeItem.icon"></i>
                                        <span>{{ threeItem.menuName}}</span>
                                    </template>
                                    <template v-for="fourItem in threeItem.children">
                                        <el-submenu :disabled="fourItem.disabled" :index="fourItem.id+''"
                                                    v-if="fourItem.children != null && fourItem.children != undefined && fourItem.children.length>0" :key="fourItem.id+''">
                                            <template #title>
                                                <i :class="fourItem.icon"></i>
                                                <span>{{ fourItem.menuName}}</span>
                                            </template>
                                        </el-submenu>
                                        <el-menu-item
                                                v-else
                                                :disabled="fourItem.disabled"
                                                :index="fourItem.url+''"
                                                :route="fourItem.url"
                                                @click="savePath(fourItem.url)"
                                                :key="fourItem.id+''"
                                        >
                                            <i :class="fourItem.icon"></i>
                                            <span>{{fourItem.menuName}}</span>
                                        </el-menu-item>
                                    </template>
                                    <!--                                    <MenuTree :menuList="item.children"></MenuTree>-->
                                </el-submenu>
                                <el-menu-item
                                        v-else
                                        :disabled="threeItem.disabled"
                                        :index="threeItem.url+''"
                                        :route="threeItem.url"
                                        @click="savePath(threeItem.url)"
                                        :key="threeItem.id+''"
                                >
                                    <i :class="threeItem.icon"></i>
                                    <span>{{threeItem.menuName}}</span>
                                </el-menu-item>
                            </template>
                        </el-submenu>
                        <el-menu-item
                                v-else
                                :disabled="subItem.disabled"
                                :index="subItem.url+''"
                                :route="subItem.url"
                                @click="savePath(subItem.url)"
                                :key="subItem.id+''"
                        >
                            <i :class="subItem.icon"></i>
                            <span>{{subItem.menuName}}</span>
                        </el-menu-item>
                    </template>
                </el-submenu>
                <el-menu-item
                        v-else
                        :disabled="item.disabled"
                        :index="item.url+''"
                        :route="item.url"
                        @click="savePath(item.url)"
                        :key="item.id+''"
                >
                    <i :class="item.icon"></i>
                    <span>{{item.menuName}}</span>
                </el-menu-item>
            </template>
        </el-menu>
    </div>
</template>

<script>
    import {computed, ref} from "vue";
    import {useRoute} from "vue-router";
    import {createNamespacedHelpers, useStore} from 'vuex'
    import {findMenu} from "../api/system/user";
    import {ElMessage} from "element-plus";


    export default {
        setup() {
            const store = useStore();
            const route = useRoute();

            let menuList = ref([]);

            const onRoutes = computed(() => {
                return route.path;
            });
            let activePath = computed(() => route.path);
            let isOpen = computed(() => store.state.component.collapse);

            const savePath = (path) => {
                window.sessionStorage.setItem("activePath", path);
                activePath = path;
            }

            /**
             加载菜单数据
             */
            const getMenuList = () => {
                findMenu().then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("获取菜单失败:" + res.data.data.errorMsg)
                    }
                    menuList.value = res.data.data;
                    store.commit("component/setMenuList", menuList);
                }).catch((res) => {
                    ElMessage.error("获取菜单失败:" + res.data.data.errorMsg)
                });

            }
            getMenuList();

            return {
                menuList,
                onRoutes,
                activePath,
                isOpen,
                getMenuList,
                savePath,

            };
        }
    };
</script>

<style>
    .sidebar {
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom: 0;
        overflow-y: scroll;
    }

    .sidebar::-webkit-scrollbar {
        width: 0;
    }

    .sidebar-el-menu:not(.el-menu--collapse) {
        width: 250px;
    }

    .sidebar > ul {
        height: 100%;
    }

    .el-menu--collapse span,
    .el-menu--collapse i.el-submenu__icon-arrow {
        height: 0;
        width: 0;
        overflow: hidden;
        visibility: hidden;
        display: inline-block;
    }

    .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 200px;
        min-height: 400px;
    }
</style>