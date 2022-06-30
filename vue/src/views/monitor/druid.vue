<template>
    <iframe :src="druidSrc"
            frameborder="no" border="0" marginwidth="0" marginheight="0"
            allowtransparency="yes" style="overflow:hidden;margin-top: 50px" height="600px;" width="100%"></iframe>
</template>
<script>

    import {ref} from "vue";

    export default {
        setup() {
            const druidSrc = ref(import.meta.env.DEV ? '/api/druid/index.html' : import.meta.env.VITE_APP_BASE_API + 'druid/index.html')

            return {
                druidSrc,
            }
        }
    }
</script>