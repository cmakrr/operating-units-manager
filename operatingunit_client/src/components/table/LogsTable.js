import {Button, Input, Space, Table} from "antd";
import {logsTableLocale} from "../../const/constants";
import React, {useRef, useState} from "react";
import {SearchOutlined,} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import {compareStrings} from "../../functions/Utils";

export const LogsTable = ({logs}) => {
    const [searchText, setSearchText] = useState("");
    const [searchedColumn, setSearchedColumn] = useState("");
    const [sortedInfo, setSortedInfo] = useState({});
    const searchInput = useRef(null);

    const entityTypeConverter = {
        "PATIENT": "Пациент",
        "WORKER": "Сотрудник",
        "OPERATION": "Операция",
        "OPERATION_FACT": "Факт операции",
        "USER": "Пользователь"
    };

    const operationTypeConverter = {
        "CREATE": "Создание",
        "EDIT": "Редактирование",
        "DELETE": "Удаление"
    };

    const options = {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false // 24-часовой формат
    };

    logs = logs?.map(log =>({
        ...log,
        logTime: new Date(log.logTime).toLocaleString('ru-RU', options),
        operationType: operationTypeConverter[log.operationType],
        affectedEntityType: entityTypeConverter[log.affectedEntityType]
    }))


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

    const logColumns = [
        {
            title: "Пользователь",
            dataIndex: "username",
            key: "username",
            width: "15%",
            ...getColumnSearchProps("username", "Пользователь"),
            sorter: (a, b) => compareStrings(a?.username, b?.username),
            sortOrder: sortedInfo.columnKey === "username" ? sortedInfo.order : null,
        },
        {
            title: "Сущность",
            dataIndex: "affectedEntityType",
            key: "affectedEntityType",
            ...getColumnSearchProps("affectedEntityType", "Сущность"),
            sorter: (a, b) => compareStrings(a?.affectedEntityType, b?.affectedEntityType),
            sortOrder: sortedInfo.columnKey === "affectedEntityType" ? sortedInfo.order : null,
        },
        {
            title: "Операция",
            key: "operationType",
            dataIndex: "operationType",
            ...getColumnSearchProps("operationType", "Операция"),
            sorter: (a, b) => compareStrings(a?.operationType, b?.operationType),
            sortOrder: sortedInfo.columnKey === "operationType" ? sortedInfo.order : null,
        },
        {
            title: "Дата и время",
            dataIndex: "logTime",
            key: "logTime",
            ...getColumnSearchProps("logTime", "Дата и время"),
            sorter: (a, b) => compareStrings(a.logTime, b.logTime),
            sortOrder:
                sortedInfo.columnKey === "logTime" ? sortedInfo.order : null,
        }
    ];

    return (
        <Table
            locale={logsTableLocale}
            columns={logColumns}
            dataSource={logs}
            onChange={handleChange}
        />
    );
};

export default LogsTable;