import MainHeader from "../../components/common/MainHeader";
import {ManagerSider} from "../../components/sider/Siders";
import {managerMenuItems} from "../../const/constants";
import React, {useEffect, useState} from "react";
import {OperationsTable} from "../../components/table/OperationsTable";
import {useNavigate} from "react-router-dom";
import {useLocation} from "react-router";
import {getOngoingOperationReport} from "../../request/ReportRequests";
import {locale} from "dayjs";
import {clientApi} from "../../const/api/clientApi";
import LocalDateIntervalPicker from "../../components/datePicker/LocalDateIntervalPicker";
import {Button} from "antd";
import {getOngoingOperations} from "../../request/Requests";

locale("ru");

const handleReportGeneration = async (startDate, endDate) => {
    try {
        await getOngoingOperationReport(startDate, endDate);
    } catch (error) {
        console.error("Произошла ошибка при получении отчета:", error);
    }
};

function StatisticsPage() {
    const [operations, setOperations] = useState([]);
    const navigate = useNavigate();
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const startDate = searchParams.get("startDate");
    const endDate = searchParams.get("endDate");

    useEffect(() => {
        async function fetchData() {
            try {
                const operationsData = await getOngoingOperations(startDate, endDate);
                setOperations(operationsData);
            } catch (error) {
                console.error("Произошла ошибка при получении операций:", error);
            }
        }

        fetchData();
    }, [startDate, endDate]);

    return (
        <MainHeader>
            <div style={{overflowX: "auto", width: "100%"}}>
                <ManagerSider
                    breadcrumb={[managerMenuItems.statistics.label]}
                    defaultKey={managerMenuItems.statistics.key}
                >
                    <div>
                        Статистика по начатым и завершенным операциям за{" "}
                        <LocalDateIntervalPicker
                            navigate={navigate}
                            url={clientApi.manager.statisticsForDates}
                            defaultStartValue={startDate}
                            defaultEndValue={endDate}
                        ></LocalDateIntervalPicker>
                        <p/>
                        <Button
                            type="primary"
                            htmlType="submit"
                            style={{width: "100%"}}
                            onClick={() => handleReportGeneration(startDate, endDate)}
                        >
                            Сформировать отчет о загруженности операционных
                        </Button>
                        <p/>
                        <OperationsTable operations={operations} statistics={true} forManager={true}/>
                    </div>
                </ManagerSider>
            </div>
        </MainHeader>
    );
}

export default StatisticsPage;
