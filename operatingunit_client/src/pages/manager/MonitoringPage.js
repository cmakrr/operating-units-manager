import MainHeader from "../../components/common/MainHeader";
import { ManagerSider } from "../../components/sider/Siders";
import { managerMenuItems } from "../../const/constants";
import React, { useEffect, useState } from "react";
import { getOperatingRoomsForMonitoring } from "../../request/OperatingRoomRequests";
import OperatingRoomCarousel from "../../components/model/operatingRoom/OperatingRoomCarousel";
import MonitoringTable from "../../components/table/MonitoringTable";

function StatisticsPage() {
  const [rooms, setRooms] = useState([]);

  useEffect(() => {
    async function fetchData() {
      try {
        let operationsData = await getOperatingRoomsForMonitoring();
        operationsData = operationsData.sort((a, b) =>
          a.name.localeCompare(b.name),
        );
        setRooms(operationsData);
      } catch (error) {
        console.error("Произошла ошибка при получении операций:", error);
      }
    }

    fetchData();

    const intervalId = setInterval(fetchData, 9000);

    return () => clearInterval(intervalId);
  }, []);

  return (
    <MainHeader>
      <div style={{ overflowX: "auto", width: "100%" }}>
        <ManagerSider
          breadcrumb={[managerMenuItems.monitoring.label]}
          defaultKey={managerMenuItems.monitoring.key}
        >
          <div>
            <OperatingRoomCarousel rooms={rooms} />
            <MonitoringTable rooms={rooms}/>
          </div>
        </ManagerSider>
      </div>
    </MainHeader>
  );
}

export default StatisticsPage;
