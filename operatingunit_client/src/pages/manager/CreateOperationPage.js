import MainHeader from "../../components/common/MainHeader";
import {ManagerSider} from "../../components/sider/Siders";
import React, {useState} from "react";
import {managerMenuItems} from "../../const/constants";
import {Button, DatePicker, Form, Input, message, Select, TimePicker} from "antd";
import dayjs from "dayjs";
import {getInfo, saveOperation} from "../../request/OperationRequests";

dayjs.locale("ru");
function CreateOperationPage() {
    const [availableResources, setAvailableResources] = useState(null);
    const [timeInfo, setTimeInfo] = useState();
    const [isTimeSubmitted, setIsTimeSubmitted] = useState(false);
    const [startTimeValue, setStartTimeValue] = useState(dayjs());
    const [endTimeValue, setEndTimeValue] = useState(dayjs());
    const [dateValue, setDateValue] = useState();

    const formatTime = (time) => {
        console.log(time);
        time = time.toString().split(' ')[4];
        const [hours, minutes] = time.split(':');
        return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:00`;
    };

    async function fetchInfo(startTimeString, endTimeString){
        const info = await getInfo(startTimeString, endTimeString);
        if(!info?.availableWorkers?.length || !info?.availableOperatingRooms?.length || !info?.availablePatients?.length){
            message.error(<span>{`Нет свободных ресурсов для провередения операции на данное время!`}</span>);
            return false;
        }
        setAvailableResources(info);
        return true;
    }

    async function submitTimeForm(values){
        const date = dayjs(values.date).format('YYYY-MM-DD');
        const formattedStartTime = dayjs(values.startTime).format('HH:mm:ss');
        const formattedEndTime =dayjs(values.endTime).format('HH:mm:ss');
        const startTimeString = `${date}T${formattedStartTime}`;
        const endTimeString = `${date}T${formattedEndTime}`;
        if(startTimeString >= endTimeString){
            message.error(<span>{`Окончание операции должно быть позже начала!`}</span>);
            return;
        }
        const hasFreeResources = await fetchInfo(startTimeString, endTimeString);
        if(hasFreeResources){
            const info = {
                date: values.date,
                startTime: startTimeString,
                endTime: endTimeString
            }
            setTimeInfo(info);
            setIsTimeSubmitted(true);
        }
    }

    async function saveOperationValues(values){
        values = {
            ...values,
            ...timeInfo
        }
        await saveOperation(values);
        message.success(<span>{`Операция сохранена!`}</span>);
        setIsTimeSubmitted(false);
    }

    return (
        <MainHeader>
            <div style={{ overflowX: "auto", width: "100%" }}>
                <ManagerSider
                    breadcrumb={
                        [managerMenuItems.plan.main.label, managerMenuItems.plan.create.label]
                    }
                    defaultKey={managerMenuItems.plan.create.key}
                >
                    <div>
                    {!isTimeSubmitted && (
                    <Form
                        name="basic"
                        labelCol={{ span: 8 }}
                        wrapperCol={{ span: 16 }}
                        onFinish={(values) => submitTimeForm(values)}
                        autoComplete="off"
                        style={{ maxWidth: '400px', margin: '0 auto' }}
                    >
                        <Form.Item name="date" label="Дата"
                                   rules={[{ required: true, message: 'Пожалуйста, выберите дату операции!' }]}>
                            <DatePicker
                                format="YYYY-MM-DD"
                                disabledDate={(current) => current && current < dayjs().startOf('day')}
                                selected={dateValue}
                                onChange={(newValue) => setDateValue(newValue)}
                            />
                        </Form.Item>
                        <Form.Item name="startTime" label="Время начала"
                                   rules={[{ required: true, message: 'Пожалуйста, выберите время!' }]}>
                            <TimePicker
                                format="HH:mm"
                                value={startTimeValue}
                                onChange={(newValue) => setStartTimeValue(newValue)}
                            />
                        </Form.Item>
                        <Form.Item name="endTime" label="Время завершения:"
                                   rules={[{ required: true, message: 'Пожалуйста, выберите время!' }]}>
                            <TimePicker
                                format="HH:mm"
                                value={endTimeValue}
                                onChange={(newValue) => setEndTimeValue(newValue)}
                            />
                        </Form.Item>
                        <Button
                            type="primary"
                            htmlType="submit"
                            style={{ width: "100%" }}
                        >
                            Подтвердить
                        </Button>
                    </Form>)}
                    {isTimeSubmitted && (
                        <Form
                            name="basic"
                            labelCol={{
                                span: 6,
                            }}
                            wrapperCol={{
                                span: 20,
                            }}
                            onFinish={(values) => saveOperationValues(values)}
                            autoComplete="off"
                        >
                            <Form.Item
                                label="Название"
                                name="operationName"
                                rules={[
                                    {
                                        required: true,
                                        message:
                                            "Пожалуйста, введите название операции!",
                                    },
                                ]}
                            >
                                <Input />
                            </Form.Item>
                            <Form.Item name="operatorId" label="Оператор">
                                <Select>
                                    {availableResources.availableWorkers.map((worker) => (
                                        <Select.Option key={worker.id} value={worker.id}>
                                            {`${worker.fullName} (${worker.id})`}
                                        </Select.Option>
                                    ))}
                                </Select>
                            </Form.Item>
                            <Form.Item name="transfusiologistId" label="Трансфузиолог">
                                <Select>
                                    {availableResources.availableWorkers.map((worker) => (
                                        <Select.Option key={worker.id} value={worker.id}>
                                            {`${worker.fullName} (${worker.id})`}
                                        </Select.Option>
                                    ))}
                                </Select>
                            </Form.Item>
                            <Form.Item name="assistantId" label="Ассистент">
                                <Select>
                                    {availableResources.availableWorkers.map((worker) => (
                                        <Select.Option key={worker.id} value={worker.id}>
                                            {`${worker.fullName} (${worker.id})`}
                                        </Select.Option>
                                    ))}
                                </Select>
                            </Form.Item>
                            <Form.Item name="operatingRoomId" label="Операционная"  rules={[
                                {
                                    required: true,
                                    message:
                                        "Пожалуйста, выберите операционную комнату!",
                                },
                            ]}>
                                <Select>
                                    {availableResources.availableOperatingRooms.map((room) => (
                                        <Select.Option key={room.id} value={room.id}>
                                            {`${room.name}`}
                                        </Select.Option>
                                    ))}
                                </Select>
                            </Form.Item>
                            <Form.Item name="patientId" label="Пациент" rules={[
                                {
                                    required: true,
                                    message:
                                        "Пожалуйста, выберите пациента!",
                                },
                            ]}>
                                <Select>
                                    {availableResources.availablePatients.map((patient) => (
                                        <Select.Option key={patient.id} value={patient.id}>
                                            {`${patient.fullName} (${patient.id})`}
                                        </Select.Option>
                                    ))}
                                </Select>
                            </Form.Item>
                            <Form.Item
                                label="Инструменты"
                                name="instruments"
                            >
                                <Input />
                            </Form.Item>
                            <Form.Item
                                label="Описание"
                                name="description"
                            >
                                <Input  />
                            </Form.Item>
                            <Form.Item
                                wrapperCol={{
                                    offset: 6,  // This matches your labelCol span
                                    span: 20,   // This matches your wrapperCol span
                                }}
                            >
                            <Button
                                type="primary"
                                onClick={(e) => {
                                    e.preventDefault();
                                    setIsTimeSubmitted(false)
                                }}
                                style={{ width: "100%" }}
                            >
                                Назад
                            </Button>
                            </Form.Item>
                            <Form.Item
                                wrapperCol={{
                                    offset: 6,  // This matches your labelCol span
                                    span: 20,   // This matches your wrapperCol span
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
                        </Form>)}
                    </div>
                </ManagerSider>
            </div>
        </MainHeader>
    );
}

export default CreateOperationPage;
