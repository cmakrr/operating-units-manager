import React from "react";
import { BarChart, LineChart, PieChart, Bar, Line, Pie, XAxis, YAxis, CartesianGrid, Tooltip, Legend, Cell, ResponsiveContainer } from 'recharts';
import {convertToHours, getPredictionValues, processOperationsData} from "./CommonMethods";
import {CalculatorOutlined, CalendarOutlined, ClockCircleOutlined, PieChartOutlined} from "@ant-design/icons";
import {Col} from "antd";
import Card from "antd/es/card/Card";
import Statistic from "antd/es/statistic/Statistic";
import { Row } from 'antd';

function AnalysisInfo(info) {
    info = info.info;
    processInfo();

    function processInfo(){
        const operations = info.operations;
        info.operations = Object.keys(operations)
            .sort()
            .reduce((acc, key) => {
                acc[key] = operations[key];
                return acc;
            }, {});
        const overallAnalysis = info.overallAnalysis;
        overallAnalysis.allOperationsTime = convertToHours(overallAnalysis.allOperationsTimeInMinutes);
        overallAnalysis.averageOperationDuration= convertToHours(overallAnalysis.averageOperationDurationInMinutes);
        overallAnalysis.averageOperationPerDay = convertToHours(overallAnalysis.averageOperationMinutesPerDay);
        overallAnalysis.averageOperationSteps = Math.round(overallAnalysis.averageOperationSteps * 100) / 100;
        overallAnalysis.averageOperationsPerDay = Math.round(overallAnalysis.averageOperationsPerDay * 100) / 100;
    }

    const {
        opsPerDayPrediction,
        avgDurationPrediction,
        opsPerRoomPrediction,
        roomTrends
    } = getPredictionValues(info.operations);

    const {
        opsPerDay,
        avgDurationPerDay,
        opsPerRoom,
        minutesPerRoom
    } = processOperationsData(info.operations);

    const analysisMetrics = [
        {
            key: 'allOperationsCount',
            title: 'Количество операций',
            icon: <PieChartOutlined />,
            suffix: ''
        },
        {
            key: 'allOperationsTime',
            title: 'Общее операционное время',
            icon: <ClockCircleOutlined />,
            suffix: 'ч.'
        },
        {
            key: 'averageOperationDuration',
            title: 'Средняя продолжительность операции',
            icon: <ClockCircleOutlined />,
            suffix: 'ч.',
            precision: 2
        },
        {
            key: 'averageOperationPerDay',
            title: 'Среднее операционное время в день',
            icon: <CalculatorOutlined />,
            suffix: 'ч.',
            precision: 2
        },
        {
            key: 'averageOperationSteps',
            title: 'Среднее количество этапов',
            icon: <CalculatorOutlined />,
            suffix: '',
            precision: 2
        },
        {
            key: 'averageOperationsPerDay',
            title: 'Операций в день',
            icon: <CalendarOutlined />,
            suffix: '',
            precision: 2
        }
    ];


    const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

    return (
        <>
            <Card title="Суммарная информация" style={{ margin: '20px 0' }}>
                <Row gutter={[16, 16]}>
                    {analysisMetrics.map((metric) => ( // Double protection
                        <Col key={metric.key} xs={24} sm={12} md={12} lg={8} xl={8}>
                            <Card bordered={false} hoverable>
                                <Statistic
                                    title={metric.title}
                                    value={info.overallAnalysis[metric.key] ?? '-'} // Show dash for missing data
                                    precision={metric.precision}
                                    suffix={metric.suffix}
                                    prefix={metric.icon}
                                    valueStyle={{
                                        fontSize: '1.5rem',
                                        fontWeight: 500,
                                        color: '#1890ff'
                                    }}
                                />
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Card>
            <div style={{
                textAlign: 'center',
                marginBottom: '30px',
                padding: '20px 0',
                borderBottom: '1px solid #e0e0e0'
            }}>
                <h1 style={{
                    fontSize: '28px',
                    fontWeight: '600',
                    color: '#2c3e50',
                    margin: '0 0 10px 0'
                }}>Операционная статистика за {info.analysisStart.replaceAll('-','.')} - {info.analysisEnd.replaceAll('-','.')}</h1>
            </div>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', padding: '20px' }}>
            <div>
                <h3>Количество операций в день</h3>
                <BarChart width={500} height={300} data={opsPerDay}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="date" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="count" fill="#8884d8" name="Операции" />
                </BarChart>
            </div>

            {/* Chart 2: Average duration per day */}
            <div>
                <h3>Средняя продолжительность операции</h3>
                <LineChart width={500} height={300} data={avgDurationPerDay}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="date" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Line type="monotone" dataKey="avgDuration" stroke="#82ca9d" name="Часы" />
                </LineChart>
            </div>

            {/* Chart 3: Operations per room */}
            <div>
                <h3>Количество операций по операционным</h3>
                <PieChart width={500} height={300}>
                    <Pie
                        data={opsPerRoom}
                        cx="50%"
                        cy="50%"
                        labelLine={false}
                        outerRadius={80}
                        fill="#8884d8"
                        dataKey="value"
                        nameKey="name"
                        label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                    >
                        {opsPerRoom.map((entry, index) => (
                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                    </Pie>
                    <Tooltip />
                </PieChart>
            </div>

            {/* Chart 4: Total minutes per room */}
            <div>
                <h3>Длительность операций в операционных</h3>
                <PieChart width={500} height={300}>
                    <Pie
                        data={minutesPerRoom}
                        cx="50%"
                        cy="50%"
                        labelLine={false}
                        outerRadius={80}
                        fill="#8884d8"
                        dataKey="value"
                        nameKey="name"
                        label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                    >
                        {minutesPerRoom.map((entry, index) => (
                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                    </Pie>
                    <Tooltip />
                </PieChart>
            </div>
        </div>
            <div style={{
                textAlign: 'center',
                marginBottom: '30px',
                padding: '20px 0',
                borderBottom: '1px solid #e0e0e0'
            }}>
                <h1 style={{
                    fontSize: '28px',
                    fontWeight: '600',
                    color: '#2c3e50',
                    margin: '0 0 10px 0'
                }}>Прогноз операционной статистики</h1>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', padding: '20px' }}>
            {/* Chart 1: Operations per day with forecast */}
            <div className="chart-container">
                <h3>Ожидаемое количество операций в день</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <LineChart data={opsPerDayPrediction}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="date" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Line
                            type="monotone"
                            dataKey="count"
                            stroke="#82ca9d"
                            name="Операции"
                            strokeWidth={4}
                            dot={false}
                            data={opsPerDayPrediction}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>

            {/* Chart 2: Average duration with forecast */}
            <div className="chart-container">
                <h3>Ожидаемая средняя длительность операции</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <LineChart data={avgDurationPrediction}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="date" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Line
                            type="monotone"
                            dataKey="avgDuration"
                            stroke="#387908"
                            name="Часы"
                            strokeWidth={4}
                            dot={false}
                            data={avgDurationPrediction}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>

            {/* Chart 3: Operations per room */}
            <div className="chart-container">
                <h3>Ожидаемое количество операций по операционным</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <PieChart>
                        <Pie
                            data={opsPerRoomPrediction}
                            cx="50%"
                            cy="50%"
                            outerRadius={80}
                            fill="#8884d8"
                            dataKey="value"
                            label={({ name, percent }) => `${name.split('(')[0]}: ${(percent * 100).toFixed(0)}%`}
                        >
                            {opsPerRoomPrediction.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                            ))}
                        </Pie>
                        <Tooltip formatter={(value, name) => [`${value} operations`, name]} />
                    </PieChart>
                </ResponsiveContainer>
            </div>

            {/* Chart 4: Room usage growth */}
            <div className="chart-container">
                <h3>Ожидаемое использование операционных</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={roomTrends}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="name" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Bar dataKey="current" fill="#8884d8" name="Текущая длительность" />
                        <Bar dataKey="predicted" fill="#82ca9d" name="Ожидаемая длительность" />
                    </BarChart>
                </ResponsiveContainer>
            </div>
        </div>
        </>
    );
}

export default AnalysisInfo;
