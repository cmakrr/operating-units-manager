import { Empty } from "antd";
import { clientApi } from "./api/clientApi";

export const primaryColor = "#00B96B";
export const blueColor = "#4096ff";
export const rnpc = "";

export const ipPattern =
  "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

export const tokenKey = "accessToken";

export const operationStatuses = {
  notStarted: "Не начата",
  started: "Начата",
  finished: "Окончена",
  notFinished: "Не окончена",
};

export const tableLocale = {
  filterConfirm: "Поиск",
  filterReset: "Сбросить",
  triggerDesc: "Нажмите для сортировки по убыванию",
  triggerAsc: "Нажмите для сортировки по возрастанию",
  cancelSort: "Нажмите для отмены сортировки",
};

export const tableOperationsRoomLocale = {
  ...tableLocale,
  emptyText: (
    <Empty
      description={`В операционном блоке сегодня нет проводимых операций.`}
    />
  ),
};

export const monitoringTableLocale = {
  ...tableLocale,
  emptyText: (
    <Empty
      description={`Для медицинского учреждения не назначены операционные блоки.`}
    />
  ),
};

export const usersTableLocale = {
  ...tableLocale,
  emptyText: (
    <Empty
      description={`Нет зарегистрированных пользователей с данной ролью.`}
    />
  ),
};

export const patientsTableLocale = {
  ...tableLocale,
  emptyText: (
      <Empty
          description={`Пациенты отсутствуют.`}
      />
  ),
};

export const logsTableLocale = {
  ...tableLocale,
  emptyText: (
      <Empty
          description={`Логи отсутствуют.`}
      />
  ),
};

export const operationsTableLocale = {
  ...tableLocale,
  emptyText: (
      <Empty
          description={`Операции отсутствуют.`}
      />
  ),
};

export const workersTableLocale = {
  ...tableLocale,
  emptyText: (
      <Empty
          description={`Пациенты отсутствуют.`}
      />
  ),
};

export const tableOperationsLocale = {
  ...tableLocale,
  emptyText: (
    <Empty
      description={`В данные календарные сутки не было проводимых и запланированных операций`}
    />
  ),
};

export const tableOperationsHistoryLocale = {
  ...tableLocale,
  emptyText: (
      <Empty
          description={`За данный период времени не было проведено операций`}
      />
  ),
};

export const operationStepStatuses = {
  process: ["STARTED"],
  finish: ["FINISHED"],
  wait: ["CANCELLED", "NOT_STARTED"],
};

export const operationStepCancelStatus = {
  cantCanceled: "Не может быть отменен",
};

export function getOperationStepStatus(value) {
  for (const key in operationStepStatuses) {
    if (operationStepStatuses[key].includes(value)) {
      return key;
    }
  }
}

export const availableOperationPlanFileFormats = {
  DOC: "application/msword",
  DOCX: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
};

export const isOperationPlanFormatAvailable = (format) => {
  return Object.values(availableOperationPlanFileFormats).includes(format);
};

export const managerMenuItems = {
  plan: {
    main: { label: "План", key: "Plan" },
    load: { label: "Загрузка", key: "PlanLoad" },
    view: { label: "Просмотр", key: "PlanView" },
    create: { label : "Создать", key: "CreatePlan" }
  },
  users: {
    main: {
      label: "Пользователи",
      key: "Users",
      list: ["TRACKER", "MANAGER", "GENERAL_MANAGER", "ADMIN"],
    },
    tracker: { label: "Оператор", key: "TRACKER" },

    manager: { label: "Менеджер", key: "MANAGER" },
    general_manager: { label: "Главный менеджер", key: "GENERAL_MANAGER" },

    admin: { label: "Администратор", key: "ADMIN" },
  },
  monitoring: { label: `Мониторинг`, key: `Monitoring` },
  operatingRooms: { label: "Операционные", key: "OperatingRooms" },
  statistics: { label: `Статистика`, key: `Statistics` },
  patients: { label: `Пациенты`, key: `Patients`},
  workers: {label: `Работники`, key: `Workers`},
  operations: {label: `История операций`, key: `Operations`},
  logs: {label: `Логи`, key: `Logs`},
  analysis: {
    main: { label: "Анализ", key: "Analysis" },
    worker: { label: "Работники", key: "WorkersAnalysis" },
    room: { label: "Палаты", key: "RoomsAnalysis"},
    operations: {label: "Операции", key: "OperationsAnalysis"}
  }
};

export const menuItemsKeyRoutes = {
  [managerMenuItems.plan.load.key]: clientApi.manager.loadPlan,
  [managerMenuItems.plan.create.key]: clientApi.manager.createPlan,
  [managerMenuItems.plan.view.key]: (date) =>
    clientApi.manager.planForDate(date),
  [managerMenuItems.statistics.key]: (startDate, endDate) =>
    clientApi.manager.statisticsForDates(startDate, endDate),
  [managerMenuItems.operatingRooms.key]: clientApi.manager.operatingRooms,
  [managerMenuItems.monitoring.key]: clientApi.manager.monitoring,
  [managerMenuItems.users.main.key]: (role) => clientApi.manager.users(role),
  [managerMenuItems.patients.key]: clientApi.manager.patients,
  [managerMenuItems.workers.key]: clientApi.manager.workers,
  [managerMenuItems.analysis.operations.key]: clientApi.manager.operationsAnalysis,
  [managerMenuItems.analysis.worker.key]: clientApi.manager.workersAnalysis,
  [managerMenuItems.analysis.room.key]: clientApi.manager.operatingRoomsAnalysis,
  [managerMenuItems.operations.key]: clientApi.manager.operations,
  [managerMenuItems.logs.key]: clientApi.manager.logs
};
