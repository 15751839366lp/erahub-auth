const system = [
    {
        path: '/system/welcome',
        name: 'Welcome',
        meta:{
            title: "系统首页"
        },
        component: () => import( '../../../views/welcome.vue'), //系统欢迎页
    },
    {
        path: "/error/401",
        name: '401',
        meta:{
            title: "错误访问"
        },
        component: () => import( '../../../views/error/401.vue'),
    },
    {
        path: "/error/404",
        name: '404',
        meta:{
            title: "错误访问"
        },
        component: () => import( '../../../views/error/404.vue'),
    },
    {
        path: '/system/users',
        name: 'Users',
        meta:{
            title: "用户管理"
        },
        component: () => import( '../../../views/system/user.vue'), //用户管理
    },
    {
        path: '/system/roles',
        name: 'Roles',
        meta:{
            title: "角色管理"
        },
        component: () => import( '../../../views/system/role.vue'), //角色管理
    },
    {
        path: '/system/menus',
        name: 'Menus',
        meta:{
            title: "菜单管理"
        },
        component: () => import( '../../../views/system/menu.vue'), //菜单管理
    },
    {
        path: '/system/departments',
        name: 'Departments',
        meta:{
            title: "部门管理"
        },
        component: () => import( '../../../views/system/dept.vue'), // 部门管理
    },
    {
        path: '/system/files',
        name: 'Files',
        meta:{
            title: "文件管理"
        },
        component: () => import('../../../views/system/file.vue'),   //文件管理
    },
    {
        path: '/system/icon',
        name: 'Icons',
        meta:{
            title: "系统图标"
        },
        component: () => import('../../../views/system/icon.vue'),  //系统图标
    },

]

export default system