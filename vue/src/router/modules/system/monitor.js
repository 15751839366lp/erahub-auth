const monitor = [
    {
        path: '/monitor/logs',
        name: 'Logs',
        meta:{
            title: "操作日志"
        },
        component: () => import( '../../../views/monitor/log.vue'),     //操作日志
    },
    {
        path: '/monitor/login-log',
        name: 'LoginLogs',
        meta:{
            title: "登入日志"
        },
            component: () => import( '../../../views/monitor/login-log.vue'), //登入日志
    },
    {
        path: '/monitor/swagger-ui',
        name: 'SwaggerUI',
        meta:{
            title: "项目接口"
        },
        component: () => import( '../../../views/monitor/swagger-ui.vue'), //项目接口
    },
    {
        path: '/monitor/druid',
        name: 'Druid',
        meta:{
            title: "SQL监控"
        },
        component: () => import( '../../../views/monitor/druid.vue'), //SQL监控
    },
]

export default monitor