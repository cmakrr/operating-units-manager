import MainHeader from "../../components/common/MainHeader";
import { ManagerSider } from "../../components/sider/Siders";
import React, { useEffect, useState } from "react";
import { managerMenuItems } from "../../const/constants";
import LocaleDatePicker from "../../components/datePicker/LocaleDatePicker";
import { useLocation } from "react-router";
import dayjs from "dayjs";
import "dayjs/locale/ru";
import { useNavigate } from "react-router-dom";
import { getOperationsForDate } from "../../request/Requests";
import { OperationsTable } from "../../components/table/OperationsTable";
import { clientApi } from "../../const/api/clientApi";

dayjs.locale("ru");
function OperationsPlanPage() {
  const [operations, setOperations] = useState([]);
  const navigate = useNavigate();
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const date = searchParams.get("date");

  useEffect(() => {
    async function fetchData() {
      if (!date) return;
      try {
        const operationsData = await getOperationsForDate(date);
        setOperations(operationsData);
      } catch (error) {
        console.error("Произошла ошибка при получении операций:", error);
      }
    }
    fetchData();
  }, [date]);

  return (
    <MainHeader>
      <div style={{ overflowX: "auto", width: "100%" }}>
        <ManagerSider
          breadcrumb={[
            managerMenuItems.plan.main.label,
            managerMenuItems.plan.view.label,
          ]}
          defaultKey={managerMenuItems.plan.view.key}
        >
          <div>
            <span>Операционный план на </span>
            <LocaleDatePicker
              defaultValue={dayjs(date)}
              navigate={navigate}
              url={clientApi.manager.planForDate}
            ></LocaleDatePicker>
            <p></p>
            <OperationsTable operations={operations} statistics={false} forManager={true}/>
          </div>
        </ManagerSider>
      </div>
    </MainHeader>
  );
}

export default OperationsPlanPage;
