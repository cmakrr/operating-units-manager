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
  },
  operations: {
    get: (id) => `/api/v1/operations/${id}`,
    getAllForDate: (date) => `/api/v1/operations?date=${date}`,
    getOngoingForDates: (startDate, endDate) =>
      `/api/v1/operations/ongoing?startDate=${startDate}&endDate=${endDate}`,
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
    dispatchById: (id) =>  `api/v1/patient/dispatch/${id}`
  }
};
