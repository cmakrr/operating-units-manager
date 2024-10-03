import MainHeader from "../../components/common/MainHeader";
import { ManagerSider } from "../../components/sider/Siders";
import React, { useEffect, useState } from "react";
import { managerMenuItems } from "../../const/constants";
import { createUser, getUsersByRole } from "../../request/UserRequests";
import { Button, Form, Input, Modal } from "antd";
import { useParams } from "react-router";
import UsersTable from "../../components/model/user/UsersTable";

function UsersByRolePage() {
  const [users, setUsers] = useState([]);
  const [modal2Open, setModal2Open] = useState(false);
  const { role } = useParams();

  useEffect(() => {
    async function fetchData() {
      try {
        const operationsData = await getUsersByRole(role);
        setUsers(operationsData);
      } catch (error) {
        console.error("Произошла ошибка при получении операций:", error);
      }
    }

    fetchData();
  }, [role]);

  return (
    <MainHeader>
      <div style={{ overflowX: "auto", width: "100%" }}>
        <ManagerSider
          breadcrumb={[
            managerMenuItems.users.main.label,
            managerMenuItems.users[`${role}`].label,
          ]}
          defaultKey={managerMenuItems.users[`${role}`].key}
        >
          {role !== managerMenuItems.users.tracker.key.toLowerCase() && (
            <Button
              type="primary"
              style={{ width: "100%" }}
              onClick={() => setModal2Open(true)}
            >
              Зарегистрировать нового пользователя с ролью
              {" " + managerMenuItems.users[`${role}`].label}
            </Button>
          )}
          <p />
          <div>
            <UsersTable users={users} role={role} />
          </div>
          <p />
          {role !== managerMenuItems.users.tracker.key.toLowerCase() && (
            <Button
              type="primary"
              style={{ width: "100%" }}
              onClick={() => setModal2Open(true)}
            >
              Зарегистрировать нового пользователя с ролью
              {" " + managerMenuItems.users[`${role}`].label}
            </Button>
          )}
          <Modal
            title={`Регистрация пользователя с ролью ${managerMenuItems.users[`${role}`].label}`}
            centered
            open={modal2Open}
            onCancel={() => setModal2Open(false)}
            footer={null}
          >
            <Form
              name="basic"
              labelCol={{
                span: 5,
              }}
              wrapperCol={{
                span: 20,
              }}
              onFinish={(values) => createUser(values)}
              autoComplete="off"
            >
              <Form.Item
                label="Роль"
                name="roleType"
                hidden
                initialValue={role?.toUpperCase()}
              >
                <Input />
              </Form.Item>
              <Form.Item
                label="Логин"
                name="login"
                rules={[
                  {
                    required: true,
                    message: `Пожалуйста, введите логин!`,
                  },
                ]}
              >
                <Input />
              </Form.Item>
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
                <Button
                  type="primary"
                  htmlType="submit"
                  style={{ width: "100%" }}
                >
                  Создать
                </Button>
              </Form.Item>
            </Form>
          </Modal>
        </ManagerSider>
      </div>
    </MainHeader>
  );
}

export default UsersByRolePage;
