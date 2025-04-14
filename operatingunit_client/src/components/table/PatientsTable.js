import {Button, Input, message, Space, Table} from "antd";
import {blueColor, patientsTableLocale} from "../../const/constants";
import React, {useRef, useState} from "react";
import {SearchOutlined,} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import {compareStrings} from "../../functions/Utils";
import {dischargePatient} from "../../request/PatientRequests";
import {compareDates, compareNumbers} from "../../utils/compareUtils";

async function dischargePatientById(id, patients, setPatients) {
    dischargePatient(id);

    message.success(<span>{`Пациент был выписан успешно!`}</span>);

    removePatient(id, patients, setPatients)
}

function removePatient(id, patients, setPatients){
    const newPatients = patients.filter(x=>x.id !== id);
    setPatients(newPatients);
}

export const PatientsTable = ({patientEntities, setPatients}) => {
    const [searchText, setSearchText] = useState("");
    const [searchedColumn, setSearchedColumn] = useState("");
    const [sortedInfo, setSortedInfo] = useState({});
    const searchInput = useRef(null);

    const patients = patientEntities?.map((patient) => ({
        ...patient,
        idd: patient.id,
        birthYear: patient.birthYear !== null ? patient.birthYear.split('-').join('.') : null
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

    const patientColumns = [
        {
            title: "Идентификационный номер",
            dataIndex: "id",
            key: "id",
            width: "10%",
            ...getColumnSearchProps("id", "Идентификационный номер"),
            sorter: (a, b) => a.id > b.id ? 1 : -1,
            sortOrder: sortedInfo.columnKey === "id" ? sortedInfo.order : null,
        },
        {
            title: "ФИО",
            dataIndex: "fullName",
            key: "fullName",
            ...getColumnSearchProps("fullName", "ФИО"),
            sorter: (a, b) => compareStrings(a?.fullName, b?.fullName),
            sortOrder: sortedInfo.columnKey === "fullName" ? sortedInfo.order : null,
        },
        {
            title: "Дата рождения",
            key: "birthYear",
            dataIndex: "birthYear",
            ...getColumnSearchProps("birthYear", "Дата рождения"),
            sorter: (a, b) => compareDates(a.birthYear, b.birthYear),
            sortOrder: sortedInfo.columnKey === "birthYear" ? sortedInfo.order : null,
        },
        {
            title: "Палата",
            dataIndex: "roomNumber",
            key: "roomNumber",
            ...getColumnSearchProps("roomNumber", "Палата"),
            sorter: (a, b) => compareNumbers(a.roomNumber, b.roomNumber),
            sortOrder:
                sortedInfo.columnKey === "roomNumber" ? sortedInfo.order : null,
        },
        {
            title: "Описание",
            dataIndex: "description",
            key: "description",
        },
        {
            title: "Управление",
            dataIndex: "idd",
            key: "idd",
            render: (idd, record) => (
                <>
                    <Button
                        style={{
                            width: "100%",
                            backgroundColor: blueColor,
                            color: "white",
                        }}
                        onClick={() => {
                            dischargePatientById(idd, patientEntities, setPatients);
                        }}
                    >
                        Выписать
                    </Button>
                </>
            )
        }
    ];

    return (
        <Table
            locale={patientsTableLocale}
            columns={patientColumns}
            dataSource={patients}
            onChange={handleChange}
        />
    );
};

export default PatientsTable;