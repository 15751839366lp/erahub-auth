<template>
    <iframe :src="swaggerSrc"
            frameborder="no" border="0" marginwidth="0" marginheight="0"
            allowtransparency="yes" style="overflow:hidden;" height="600px;" width="100%"></iframe>
</template>

<script>

    import {ref} from "vue";
    import { onBeforeRouteLeave, onBeforeRouteUpdate } from 'vue-router'

    export default {
        setup() {
            const swaggerSrc = ref(import.meta.env.DEV ? '/api/swagger-ui.html' : import.meta.env.VITE_APP_BASE_API + '/swagger-ui.html')

            onBeforeRouteLeave((to) => {
                swaggerSrc.value = "";
            });

            return {
                swaggerSrc,
            }
        }
    }
</script>

