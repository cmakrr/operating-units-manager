import React, {useState} from "react";
import dayjs from "dayjs";
import AnalysisInfo from "./AnalysisInfo";
import {Button, Form, Input} from "antd";
import {managerMenuItems} from "../../const/constants";
import MainHeader from "../../components/common/MainHeader";
import {ManagerSider} from "../../components/sider/Siders";
import AnalysisDatePicker from "./AnalysisDatePicker";
import {getRoomsAnalysis} from "../../request/AnalysisRequests";
import {createDateRange, validateDates} from "./CommonMethods";

function RoomsAnalysis() {
    const [startDate, setStartDate] = useState(dayjs().add(-1, "d"));
    const [endDate, setEndDate] = useState(dayjs());
    const [analysisInfo, setAnalysisInfo] = useState();
    const [isInfoFetched, setIsInfoFetched] = useState(false);

    async function submitForm(values) {
        if(!validateDates(startDate, endDate)){
            return;
        }
        const dateRange = createDateRange(startDate, endDate);

        const result = await getRoomsAnalysis(values.operationRoom, dateRange);

        if (result) {
            setAnalysisInfo(result);
            setIsInfoFetched(true);
        }
    }


    return (
        <MainHeader>
            <div style={{overflowX: "auto", width: "100%"}}>
                <ManagerSider
                    breadcrumb={
                        [managerMenuItems.analysis.main.label, managerMenuItems.analysis.room.label]
                    }
                    defaultKey={managerMenuItems.analysis.room.key}
                >
                    <div>
                        {isInfoFetched ?
                            (<>
                                <AnalysisInfo info={analysisInfo}/>
                                <Button onClick={() => setIsInfoFetched(false)}>Назад</Button>
                            </>)
                            : (<>
                                <Form
                                    name="basic"
                                    labelCol={{span: 10}}
                                    wrapperCol={{span: 16}}
                                    onFinish={(values) => submitForm(values)}
                                    autoComplete="off"
                                    style={{maxWidth: '400px', margin: '0 auto'}}
                                >
                                    <AnalysisDatePicker startDate={startDate} endDate={endDate}
                                                        setStartDate={setStartDate} setEndDate={setEndDate}/>
                                    <Form.Item
                                        label="Операционная"
                                        name="operationRoom"
                                        rules={[
                                            {
                                                required: true,
                                                message:
                                                    "Пожалуйста, введите название операционной комнаты!",
                                            },
                                        ]}
                                    >
                                        <Input/>
                                    </Form.Item>
                                    <Button
                                        type="primary"
                                        htmlType="submit"
                                        style={{ width: "50%" }}
                                    >
                                        Подтвердить
                                    </Button>
                                </Form>
                            </>)}
                    </div>
                </ManagerSider>
            </div>
        </MainHeader>
    );
}

export default RoomsAnalysis;
