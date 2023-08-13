import {createRouter, createWebHistory} from 'vue-router'
import {unauthorized} from "@/api";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'welcome',
            component: () => import('@/views/Welcome.vue'),
            children: [
                {
                    path: '',
                    name: 'welcome-login',
                    component: () => import('@/views/welcome/Login.vue'),
                },
                {
                    path: 'register',
                    name: 'welcome-register',
                    component: () => import('@/views/welcome/Register.vue'),
                },
                {
                    path: 'reset',
                    name: 'welcome-reset',
                    component: () => import('@/views/welcome/Reset.vue'),
                }
            ]
        },
        {
            path: '/index',
            name: 'index',
            component: () => import('@/views/Index.vue'),
        }
    ]
})

router.beforeEach((to, from, next) => {
    const isUnauthorized = unauthorized()
    if (to.name.startsWith('welcome-') && !isUnauthorized) next('/index')
    else if (to.fullPath.startsWith('/index') && isUnauthorized) next('/')
    else next()
})

export default router