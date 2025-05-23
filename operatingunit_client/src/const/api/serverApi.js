export const serverApi = {
  login: "api/v1/auth/login",
  operatingRoom: {
    name: "/api/v1/operatingRoom/name",
    create: `/api/v1/operatingRoom`,
    room: (id) => `/api/v1/operatingRoom/${id}`,
    ip: (id) => `/api/v1/operatingRoom/${id}/ip`,
    monitoring: "/api/v1/operatingRoom/monitoring",
    rooms: `api/v1/operatingRoom`,
  },
  tracker: {
    todayOperations: "/api/v1/tracker/operatingRoom/operations/today",
    operation: (id) => `/api/v1/tracker/operatingRoom/operations/${id}`,
    createOperationFact: (id) =>
      `/api/v1/tracker/operatingRoom/operations/${id}/fact`,
    getOperationFact: (id, factId) =>
      `/api/v1/tracker/operatingRoom/operations/${id}/fact/${factId}`,
    updateOperationFactInfo: (id, factId) =>
      `/api/v1/tracker/operatingRoom/operations/${id}/fact/${factId}`,
    startOperationFact: (id, factId) =>
      `/api/v1/tracker/operatingRoom/operations/${id}/fact/${factId}/start`,
    cancelOperationFactStart: (id, factId) =>
      `/api/v1/tracker/operatingRoom/operations/${id}/fact/${factId}/cancel`,
    startNextOperationFactStep: (id, factId) =>
      `/api/v1/tracker/operatingRoom/operations/${id}/fact/${factId}/steps/next`,
    updateOperationFactStep: (id, factId, stepId) =>
      `/api/v1/tracker/operatingRoom/operations/${id}/fact/${factId}/steps/${stepId}`,
    cancelFinishOperationFactStep: (id, factId, stepId) =>
      `/api/v1/tracker/operatingRoom/operations/${id}/fact/${factId}/steps/${stepId}/cancel`,
    getCurrentOperationFactStep: (id, factId) =>
      `/api/v1/tracker/operatingRoom/operations/${id}/fact/${factId}/steps/current`,
    finishOperationFact: (id, factId) =>
      `/api/v1/tracker/operatingRoom/operations/${id}/fact/${factId}/finish`,
  },
  operationPlan: {
    load: `/api/v1/operationPlan/load`,
    info: `/api/v1/operationPlan/info`,
    create:`/api/v1/operationPlan/create`
  },
  operations: {
    get: (id) => `/api/v1/operations/${id}`,
    getAllForDate: (date) => `/api/v1/operations?date=${date}`,
    getOngoingForDates: (startDate, endDate) =>
      `/api/v1/operations/ongoing?startDate=${startDate}&endDate=${endDate}`,
    getBetweenDates: `/api/v1/operations/dates`
  },
  logs: {
    getBetweenDates: `/api/v1/logs`
  },
  report: {
    operation: (id) => `/api/v1/report/operations/${id}`,
    ongoingOperations: (startDate, endDate) =>
      `/api/v1/report/operations/ongoing?startDate=${startDate}&endDate=${endDate}`,
  },
  users: {
    getByRole: (role) => `/api/v1/appUsers/${role}`,
    create: `api/v1/auth/register-user`,
    userById: (id) => `api/v1/appUsers/${id}`,
  },
  patient: {
    getAllInHospital: `api/v1/patient/all`,
    dispatchById: (id) =>  `api/v1/patient/dispatch/${id}`,
    save: `api/v1/patient`,
    update: `api/v1/patient`
  },
  worker: {
    getAll : `api/v1/worker/all/employed`,
    update : `api/v1/worker/update`,
    save : `api/v1/worker/save`
  },
  analysis: {
    workers: (id) => `api/v1/analysis/worker/${id}`,
    rooms: (name) => `api/v1/analysis/room/${name}`,
    operations: `api/v1/analysis`
  }
};
