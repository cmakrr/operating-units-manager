import React from "react";
import {ConfigProvider, DatePicker, Form} from "antd";
import locale from "antd/locale/ru_RU";
import dayjs from "dayjs";

function AnalysisDatePicker({startDate, setStartDate, endDate, setEndDate}) {

    return (
        <ConfigProvider locale={locale}>
            <Form.Item name="startDate" label="Начало"
                       rules={[{ required: true, message: 'Пожалуйста, выберите дату начала анализа!' }]}>
                <DatePicker
                    format="YYYY-MM-DD"
                    disabledDate={(current) => current && current > dayjs().startOf('day') && current > endDate}
                    selected={startDate}
                    onChange={(newValue) => setStartDate(newValue)}
                />
            </Form.Item>
            <Form.Item name="endDate" label="Конец"
                       rules={[{ required: true, message: 'Пожалуйста, выберите дату операции!' }]}>
                <DatePicker
                    format="YYYY-MM-DD"
                    disabledDate={(current) => current && current > dayjs().startOf('day') && current < startDate}
                    selected={endDate}
                    onChange={(newValue) => setEndDate(endDate)}
                />
            </Form.Item>
        </ConfigProvider>
    );
}

export default AnalysisDatePicker;
