import MainHeader from "../../components/common/MainHeader";
import {ManagerSider} from "../../components/sider/Siders";
import React, {useEffect, useState} from "react";
import {ipPattern, managerMenuItems} from "../../const/constants";
import {getOperatingRooms} from "../../request/OperatingRoomRequests";
import OperatingRoomTable from "../../components/table/OperatingRoomTable";
import {Button, Form, Input, Modal} from "antd";
import {createOperatingRoom} from "../../request/OperatingRoomRequests";

function OperatingRoomPage() {
  const [rooms, setRooms] = useState([]);
  const [modal2Open, setModal2Open] = useState(false);

  useEffect(() => {
    async function fetchData() {
      try {
        const operationsData = await getOperatingRooms();
        setRooms(operationsData);
      } catch (error) {
        console.error("Произошла ошибка при получении операций:", error);
      }
    }
    fetchData();
  }, []);

  return (
    <MainHeader>
      <div style={{ overflowX: "auto", width: "100%" }}>
        <ManagerSider
          breadcrumb={[managerMenuItems.operatingRooms.label]}
          defaultKey={managerMenuItems.operatingRooms.key}
        >
          <Button
            type="primary"
            style={{ width: "100%" }}
            onClick={() => setModal2Open(true)}
          >
            Добавить операционный блок
          </Button>
          <p />
          <div>
            <OperatingRoomTable rooms={rooms} />
          </div>
          <p />
          <Button
            type="primary"
            style={{ width: "100%" }}
            onClick={() => setModal2Open(true)}
          >
            Добавить операционный блок
          </Button>
          <Modal
            title="Добавление операционного блока"
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
              onFinish={(values) => createOperatingRoom(values)}
              autoComplete="off"
            >
              <Form.Item
                label="Название"
                name="name"
                rules={[
                  {
                    required: true,
                    message:
                      "Пожалуйста, введите название операционного блока!",
                  },
                ]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                label="IP-адрес"
                name="ipAddress"
                rules={[
                  {
                    pattern: ipPattern,
                    message: "Неверное значение IP-адреса!",
                  },
                ]}
              >
                <Input />
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

export default OperatingRoomPage;
