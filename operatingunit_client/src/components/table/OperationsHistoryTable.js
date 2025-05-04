import {Button, Input, Space, Table} from "antd";
import {tableOperationsHistoryLocale,} from "../../const/constants";
import React, {useRef, useState} from "react";
import {Link} from "react-router-dom";
import {clientApi} from "../../const/api/clientApi";
import {SearchOutlined} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import {compareStrings} from "../../functions/Utils";

function getMedicalWorkersInfo(operation) {
    return (
        getMedicalWorkerInfo(operation?.operator) +
        getMedicalWorkerInfo(operation?.assistant) +
        getMedicalWorkerInfo(operation?.transfusiologist)
    );
}

function getMedicalWorkerInfo(worker) {
    const result =
        worker && (worker?.fullName || worker?.role)
            ? worker?.fullName || worker?.role
            : "-";
    return result + "\n";
}

const options = {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false // 24-часовой формат
};

function convertTime(time) {
    return new Date(time).toLocaleString('ru-RU', options)
}

export const OperationsHistoryTable = ({operations}) => {
    const [searchText, setSearchText] = useState("");
    const [searchedColumn, setSearchedColumn] = useState("");
    const [sortedInfo, setSortedInfo] = useState({});
    const searchInput = useRef(null);

    operations = operations?.map((operation) => ({
        ...operation,
        operationPlanStartTime: convertTime(operation?.operationPlan?.startTime),
        operationStartTime: convertTime(operation?.operationFact?.startTime),
        operationPlanEndTime: convertTime(operation?.operationPlan?.endTime),
        operationEndTime: convertTime(operation?.operationFact?.endTime),
        planMedicalWorkers: getMedicalWorkersInfo(operation?.operationPlan),
        medicalWorkers: getMedicalWorkersInfo(operation?.operationFact),
        operatingRoomName: operation?.operatingRoom.name,
        planInstruments: operation.operationPlan.instruments,
        instruments: operation.operationFact.instruments,
        isInTime: operation.operationFact.endTime < operation.operationPlan.endTime ? "Да" : "Нет"
    }));
    const handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        setSearchText(selectedKeys[0]);
        setSearchedColumn(dataIndex);
    };
    const handleReset = (clearFilters) => {
        clearFilters();
        setSearchText("");
    };
    const handleChange = (pagination, filters, sorter) => {
        setSortedInfo(sorter);
    };

    const getColumnSearchProps = (dataIndex, dataName) => ({
        filterDropdown: ({
                             setSelectedKeys,
                             selectedKeys,
                             confirm,
                             clearFilters,
                             close,
                         }) => (
            <div
                style={{
                    padding: 8,
                }}
                onKeyDown={(e) => e.stopPropagation()}
            >
                <Input
                    ref={searchInput}
                    placeholder={`Поиск по ${dataName}`}
                    value={selectedKeys[0]}
                    onChange={(e) =>
                        setSelectedKeys(e.target.value ? [e.target.value] : [])
                    }
                    onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{
                        marginBottom: 8,
                        display: "block",
                    }}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined/>}
                        size="small"
                        style={{
                            width: 90,
                        }}
                    >
                        Поиск
                    </Button>
                    <Button
                        onClick={() =>
                            clearFilters &&
                            handleReset(clearFilters) &&
                            handleSearch(selectedKeys, confirm, dataIndex)
                        }
                        size="small"
                        style={{
                            width: 90,
                        }}
                    >
                        Очистить
                    </Button>
                    <Button
                        type="primary"
                        danger
                        size="small"
                        onClick={() => {
                            close();
                        }}
                    >
                        Закрыть
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: (filtered) => (
            <SearchOutlined
                style={{
                    color: filtered ? "#1677ff" : undefined,
                }}
            />
        ),
        onFilter: (value, record) =>
            record[dataIndex].toString().toLowerCase().includes(value.toLowerCase()),
        onFilterDropdownOpenChange: (visible) => {
            if (visible) {
                setTimeout(() => searchInput.current?.select(), 100);
            }
        },
        render: (text) =>
            searchedColumn === dataIndex ? (
                <Highlighter
                    highlightStyle={{
                        backgroundColor: "#ffc069",
                        padding: 0,
                    }}
                    searchWords={[searchText]}
                    autoEscape
                    textToHighlight={text ? text.toString() : ""}
                />
            ) : (
                text
            ),
    });

    const operationColumns = [
        {
            title: "Название операции",
            dataIndex: "operationName",
            key: "operationName",
            width: "25%",
            ...getColumnSearchProps("operationName", "Название операции"),
            sorter: (a, b) => compareStrings(a?.operationName, b?.operationName),
            sortOrder:
                sortedInfo.columnKey === "operationName" ? sortedInfo.order : null,
            render: (text, record) => (
                <Link to={clientApi.manager.operation(record.id)}>{text}</Link>
            ),
        },
        {
            title: "План. время начала",
            dataIndex: "operationPlanStartTime",
            key: "operationPlanStartTime",
            width: "5%",
            ...getColumnSearchProps("operationPlanStartTime", "Планируемое время начала"),
            sorter: (a, b) =>
                a.operationPlanStartTime.localeCompare(b.operationPlanStartTime),
            sortOrder:
                sortedInfo.columnKey === "operationPlanStartTime" ? sortedInfo.order : null,
        },
        {
            title: "Время начала",
            dataIndex: "operationStartTime",
            key: "operationStartTime",
            width: "5%",
            ...getColumnSearchProps("operationStartTime", "Время начала"),
            sorter: (a, b) =>
                a.operationStartTime.localeCompare(b.operationStartTime),
            sortOrder:
                sortedInfo.columnKey === "operationStartTime" ? sortedInfo.order : null,
            render: (text, record) => (
                <div  style={{
                    color: record.operationStartTime > record.operationPlanStartTime ? "darkorange" : "black",
                }}>
                    {text}
                </div> )
        },
        {
            title: "План. время окончания",
            dataIndex: "operationPlanEndTime",
            key: "operationPlanEndTime",
            width: "5%",
            ...getColumnSearchProps("operationPlanEndTime", "Планируемое время окончания"),
            sorter: (a, b) => a.operationPlanEndTime.localeCompare(b.operationPlanEndTime),
            sortOrder:
                sortedInfo.columnKey === "operationPlanEndTime" ? sortedInfo.order : null,
        },
        {
            title: "Время окончания",
            dataIndex: "operationEndTime",
            key: "operationEndTime",
            width: "5%",
            ...getColumnSearchProps("operationEndTime", "Время окончания"),
            sorter: (a, b) => a.operationEndTime.localeCompare(b.operationEndTime),
            sortOrder:
                sortedInfo.columnKey === "operationEndTime" ? sortedInfo.order : null,
            render: (text, record) => (
                <div  style={{
                    color: record.operationEndTime > record.operationPlanEndTime ? "darkorange" : "black",
                }}>
                    {text}
                </div> )
        },
        {
            title: (
                <>
                    План. Оператор <br/> Ассистент <br/> Трансфуз.
                </>
            ),
            dataIndex: "planMedicalWorkers",
            width: "15%",
            ...getColumnSearchProps("planMedicalWorkers", "Планируемый медицинские работники"),
            render: (medicalWorkers) => (
                <div>
                    {medicalWorkers?.split("\n").map((worker, index) => (
                        <div key={index}>{worker}</div>
                    ))}
                </div>
            ),
        },
        {
            title: (
                <>
                    Оператор <br/> Ассистент <br/> Трансфуз.
                </>
            ),
            dataIndex: "medicalWorkers",
            width: "15%",
            ...getColumnSearchProps("medicalWorkers", "Медицинские работники"),
            render: (medicalWorkers, record) => (
                <div style={{
                    color: record.medicalWorkers !== record.planMedicalWorkers ? "darkorange" : "black",
                }}>
                    {medicalWorkers?.split("\n").map((worker, index) => (
                        <div key={index}>{worker}</div>
                    ))}
                </div>
            ),
        },
        {
            title: "План. инстр.",
            dataIndex: "planInstruments",
            key: "planInstruments",
            width: "5%",
            ...getColumnSearchProps("planInstruments", "Планируемые инструменты"),
            sortOrder:
                sortedInfo.columnKey === "planInstruments" ? sortedInfo.order : null,
        },
        {
            title: "Инстр.",
            dataIndex: "instruments",
            key: "instruments",
            width: "5%",
            ...getColumnSearchProps("instruments", "Инструменты"),
            sortOrder:
                sortedInfo.columnKey === "instruments" ? sortedInfo.order : null,
            render: (text, record) => (
                <div  style={{
                    color: record.instruments !== record.planInstruments ? "darkorange" : "black",
                }}>
                    {text}
                </div> )
        },
        {
            title: "В срок",
            dataIndex: "isInTime",
            key: "isInTime",
            ...getColumnSearchProps("isInTime", "Закончена вовремя"),
            sorter: (a, b) => compareStrings(a?.isInTime, b?.isInTime),
            sortOrder:
                sortedInfo.columnKey === "isInTime" ? sortedInfo.order : null,
        },
    ]

    return (
        <Table
            locale={tableOperationsHistoryLocale}
            columns={operationColumns}
            dataSource={operations}
            onChange={handleChange}
        />
    );
};
