import axios from "axios";

function applyAxiosInterceptor(store, router) {
    axios.interceptors.request.use(config => {
        return config;
    });
    axios.interceptors.response.use(
        (response) => {
            return response;
        },
        (data) => {
            let response = data.response;

            // если нет вообще сети
            if ("ERR_NETWORK" === data.code) {
                let payload = {
                    level: "ERROR",
                    description: "Сервис не доступен. Проверьте подключение к сети Интернет или повторите действие позднее",
                    stacktrace: []
                };

                // отображим через j-exception
                store.dispatch('setException', payload);
                return Promise.reject(response);
            }

            let exception = response.data;

            // если указано что ошибка для отображения через уведомления
            if (exception.informative) {
                let payload = {
                    level: exception.level,
                    message: exception.message,
                }

                // отобразим через всплывающие уведомления
                store.dispatch('setNotification', payload);
                return Promise.reject(response);
            }

            // если мы не авторизованы
            if (response.status === 401) {

                // просто перейдём на страницу входа
                router.replace({name: "login"});
                return Promise.reject(response);
            }

            // если у нас нет прав доступа
            if (response.status === 403) {
                let payload = {
                    level: "ERROR",
                    message: "Отказано в доступе",
                }

                // отобразим через всплывающие уведомления
                store.dispatch('setNotification', payload);
                return Promise.reject(response);
            }

            // если просто возникла 500-ая ошибка на сервере
            if (!!exception.message || !!exception.stacktrace) {
                let payload = {
                    level: "ERROR",
                    description: "Внутренняя ошибка сервиса",
                    stacktrace: []
                };
                if (!!exception.level) {
                    payload.level = exception.level;
                }
                if (!!exception.message) {
                    payload.description = exception.message;
                }
                if (!!exception.stacktrace) {
                    payload.stacktrace = exception.stacktrace;
                }

                // отобразим через j-exception
                store.dispatch('setException', payload);
            }
            return Promise.reject(response);
        });
}


export default {
    install(Vue, options) {
        const axiosHost = process.env.VUE_APP_SERVER === 'remote' ? process.env.VUE_APP_HOST : window.location.host;
        axios.defaults.baseURL = process.env.VUE_APP_PROTOCOL + '://' + axiosHost + process.env.VUE_APP_CONTEXT;
        axios.defaults.withCredentials = true;
        axios.defaults.hostURL = process.env.VUE_APP_PROTOCOL + '://' + axiosHost;
        applyAxiosInterceptor(options.store, options.router);
    }
}
