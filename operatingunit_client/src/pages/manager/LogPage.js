import MainHeader from "../../components/common/MainHeader";
import {ManagerSider} from "../../components/sider/Siders";
import {managerMenuItems} from "../../const/constants";
import React, {useEffect, useState} from "react";
import dayjs from "dayjs";
import {rangePresets} from "../../components/datePicker/LocalDateIntervalPicker";
import {ConfigProvider, DatePicker} from "antd";
import {createDateRange} from "../analysis/CommonMethods";
import {getLogs} from "../../request/LogsRequests";
import LogsTable from "../../components/table/LogsTable";
import locale from "antd/locale/ru_RU";
const { RangePicker } = DatePicker;


function LogPage() {
    const [logs, setLogs] = useState([]);
    const [startDate, setStartDate] = useState(dayjs());
    const [endDate, setEndDate] = useState(dayjs());

    useEffect(() => {
        async function fetchData() {
            const dateRange = createDateRange(startDate, endDate);
            try {
                const logs = await getLogs(dateRange);
                setLogs(logs);
            } catch (error) {
                console.error("Произошла ошибка при получении операций:", error);
            }
        }

        fetchData();
    }, [startDate, endDate]);

    function onRangeChange(values) {
        setStartDate(dayjs(values[0]).format(`YYYY-MM-DD`));
        setEndDate(dayjs(values[1]).format(`YYYY-MM-DD`));
    }

    const disabledDate = (current) => {
        return current && current > dayjs().endOf("day");
    };

    return (
        <MainHeader>
            <div style={{overflowX: "auto", width: "100%"}}>
                <ManagerSider
                    breadcrumb={[managerMenuItems.logs.label]}
                    defaultKey={managerMenuItems.logs.key}
                >
                    <div>
                        Логи за {" "}
                        <ConfigProvider locale={locale}>
                            <RangePicker
                                presets={rangePresets}
                                onChange={onRangeChange}
                                disabledDate={disabledDate}
                                defaultValue={[startDate, endDate]}
                            />
                        </ConfigProvider>
                        <LogsTable logs={logs}/>
                    </div>
                </ManagerSider>
            </div>
        </MainHeader>
    );
}

export default LogPage;
