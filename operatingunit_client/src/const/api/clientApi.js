export const clientApi = {
  login: {
    base: `/login`,
    user: `/user/login`,
    operatingRoom: `/operatingRoom/login`,
  },
  operatingRoom: {
    operations: `/operatingRoom/operations`,
    operation: (id) => `/operatingRoom/operations/${id}`,
    editOperationFact: (id, factId) =>
      `/operatingRoom/operations/${id}/fact/${factId}/edit`,
    operationFactCheckout: (id, factId) =>
      `/operatingRoom/operations/${id}/fact/checkout/${factId}`,
    operationStep: (id, factId, stepId) =>
      `/operatingRoom/operations/${id}/fact/${factId}/step/${stepId}`,
  },
  manager: {
    plan: `/management/plan`,
    planForDate: (date) => `/management/plan?date=${date}`,
    loadPlan: `/management/plan/load`,
    createPlan: `/management/plan/create`,
    operatingRooms: `/management/operatingRooms`,
    operation: (id) => `/management/operation/${id}`,
    statistics: `/management/statistics`,
    statisticsForDates: (startDate, endDate) =>
      `/management/statistics?startDate=${startDate}&endDate=${endDate}`,
    monitoring: `/management/monitoring`,
    users: (role) => `/management/users/${role}`,
    patients: `/management/patients`,
    workers: `/management/workers`,
    operatingRoomsAnalysis: `/management/analysis/rooms`,
    workersAnalysis: `/management/analysis/workers`,
    operationsAnalysis: `/management/analysis/operations`
  },
};
