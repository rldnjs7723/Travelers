const mapStore = {
    namespaced: true,
    state: {
        map: null,
    },
    getters: {},
    mutations: {
        SET_MAP: (state, map) => {
            state.map = map;
        },
    },
    actions: {
        setMap({ commit }, map) {
            commit("SET_MAP", map);
        },
    },
};

export default mapStore;
