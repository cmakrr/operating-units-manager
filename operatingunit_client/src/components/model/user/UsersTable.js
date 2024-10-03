import { Button, Form, Input, Modal, Space, Table } from "antd";
import React, { useRef, useState } from "react";
import { SearchOutlined } from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import {
  blueColor,
  managerMenuItems,
  usersTableLocale,
} from "../../../const/constants";
import {
  deleteUser,
  updateUserPassword,
} from "../../../request/UserRequests";
import {compareStrings} from "../../../functions/Utils";

export const UsersTable = ({ users, role }) => {
  const [searchText, setSearchText] = useState("");
  const [searchedColumn, setSearchedColumn] = useState("");
  const [sortedInfo, setSortedInfo] = useState({});
  const searchInput = useRef(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedId, setSelectedId] = useState(null);
  const [form] = Form.useForm();

  users = users?.map((user) => ({
    ...user,
    idd: user?.id,
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
            icon={<SearchOutlined />}
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

  const userColumns = [
    {
      title: "Логин",
      dataIndex: "login",
      key: "login",
      width: "5%",
      ...getColumnSearchProps("login", "Логин"),
      sorter: (a, b) => compareStrings(a?.login, b?.login),
      sortOrder: sortedInfo.columnKey === "login" ? sortedInfo.order : null,
    },
    {
      title: "Дата регистрации",
      key: "registrationDate",
      dataIndex: "registrationDate",
      width: "5%",
      ...getColumnSearchProps("registrationDate", "Дата регистрации"),
      sorter: (a, b) => a?.registrationDate?.localeCompare(b?.registrationDate),
      sortOrder:
        sortedInfo.columnKey === "registrationDate" ? sortedInfo.order : null,
    },
    {
      title: "Управление",
      dataIndex: "idd",
      key: "idd",
      width: "1%",
      render: (idd, record) => (
        <>
          <Button
            style={{
              width: "100%",
              backgroundColor: blueColor,
              color: "white",
            }}
            onClick={() => {
              setSelectedId(idd);
              setModalOpen(true);
            }}
          >
            Настроить
          </Button>
        </>
      ),
    },
  ];

  return (
    <>
      <Table
        locale={usersTableLocale}
        columns={userColumns}
        dataSource={users}
        onChange={handleChange}
      />
      <Modal
        title={`Управление пользователем с ролью ${managerMenuItems.users[`${role}`].label}`}
        centered
        open={modalOpen}
        onCancel={() => setModalOpen(false)}
        footer={null}
        s
        afterClose={() => {
          setSelectedId(null);
        }}
      >
        <Form
          form={form}
          name="basic"
          labelCol={{
            span: 5,
          }}
          wrapperCol={{
            span: 20,
          }}
          onFinish={(values) =>
            updateUserPassword(selectedId, values.password, role)
          }
          autoComplete="off"
        >
          <Form.Item
            label="Пароль"
            name="password"
            rules={[
              {
                required: true,
                message: "Пожалуйста, введите пароль!",
              },
            ]}
          >
            <Input.Password />
          </Form.Item>

          <Form.Item
            wrapperCol={{
              offset: 5,
              span: 20,
            }}
          >
            <Button type="primary" htmlType="submit" style={{ width: "100%" }}>
              Сохранить пароль
            </Button>
          </Form.Item>
          <Form.Item
            wrapperCol={{
              offset: 5,
              span: 20,
            }}
          >
            {role !== managerMenuItems.users.tracker.key.toLowerCase() && (
              <Button
                type="primary"
                danger
                style={{ width: "100%" }}
                onClick={() => deleteUser(selectedId, role)}
              >
                Удалить пользователя
              </Button>
            )}
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default UsersTable;
