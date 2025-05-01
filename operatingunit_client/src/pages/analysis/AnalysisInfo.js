import React from "react";
import { BarChart, LineChart, PieChart, Bar, Line, Pie, XAxis, YAxis, CartesianGrid, Tooltip, Legend, Cell, ResponsiveContainer } from 'recharts';
import {getPredictionValues, processOperationsData} from "./CommonMethods";
import {CalculatorOutlined, CalendarOutlined, ClockCircleOutlined, PieChartOutlined} from "@ant-design/icons";
import {Col} from "antd";
import Card from "antd/es/card/Card";
import Statistic from "antd/es/statistic/Statistic";
import Row from "antd/es/descriptions/Row";

function AnalysisInfo(info) {
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

        const metrics = [
            {
                key: 'allOperationsCount',
                title: 'Total Operations',
                icon: <PieChartOutlined/>,
                suffix: ''
            },
            {
                key: 'allOperationsTimeInMinutes',
                title: 'Total Operation Time',
                icon: <ClockCircleOutlined/>,
                suffix: 'minutes'
            },
            {
                key: 'averageOperationDurationInMinutes',
                title: 'Avg Duration',
                icon: <ClockCircleOutlined/>,
                suffix: 'minutes',
                precision: 1
            },
            {
                key: 'averageOperationMinutesPerDay',
                title: 'Avg Minutes/Day',
                icon: <CalculatorOutlined/>,
                suffix: 'minutes',
                precision: 1
            },
            {
                key: 'averageOperationSteps',
                title: 'Avg Steps',
                icon: <CalculatorOutlined/>,
                suffix: '',
                precision: 1
            },
            {
                key: 'averageOperationsPerDay',
                title: 'Avg Operations/Day',
                icon: <CalendarOutlined/>,
                suffix: '',
                precision: 1
            }
        ];

    const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

    return (
        <>
            <Card title="Operations Summary" style={{ margin: '20px 0' }}>
                <Row gutter={16}>
                    {metrics.map((metric) => (
                        <Col xs={24} sm={12} md={8} lg={8} xl={8} key={metric.key}>
                            <Card bordered={false} style={{ marginBottom: 16 }}>
                                <Statistic
                                    title={metric.title}
                                    value={info.overallAnalysis[metric.key]}
                                    precision={metric.precision}
                                    suffix={metric.suffix}
                                    prefix={metric.icon}
                                    valueStyle={{ fontSize: '1.2rem' }}
                                />
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Card>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', padding: '20px' }}>
            <div>
                <h3>Number of Operations Per Day</h3>
                <BarChart width={500} height={300} data={opsPerDay}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="date" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="count" fill="#8884d8" name="Operations" />
                </BarChart>
            </div>

            {/* Chart 2: Average duration per day */}
            <div>
                <h3>Average Operation Duration (Minutes)</h3>
                <LineChart width={500} height={300} data={avgDurationPerDay}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="date" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Line type="monotone" dataKey="avgDuration" stroke="#82ca9d" name="Minutes" />
                </LineChart>
            </div>

            {/* Chart 3: Operations per room */}
            <div>
                <h3>Operations per Room</h3>
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
                <h3>Total Operation Minutes per Room</h3>
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
            {/* Chart 1: Operations per day with forecast */}
            <div className="chart-container">
                <h3>Daily Operations (With 3-Day Forecast)</h3>
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
                            stroke="#8884d8"
                            name="Actual"
                            strokeWidth={2}
                            dot={{ r: 4 }}
                        />
                        <Line
                            type="monotone"
                            dataKey="count"
                            stroke="#82ca9d"
                            name="Predicted"
                            strokeDasharray="5 5"
                            strokeWidth={2}
                            dot={false}
                            data={opsPerDayPrediction.filter(d => d.isPredicted)}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>

            {/* Chart 2: Average duration with forecast */}
            <div className="chart-container">
                <h3>Average Duration Trend (Minutes)</h3>
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
                            stroke="#ff7300"
                            name="Actual"
                            strokeWidth={2}
                        />
                        <Line
                            type="monotone"
                            dataKey="avgDuration"
                            stroke="#387908"
                            name="Predicted"
                            strokeDasharray="5 5"
                            dot={false}
                            data={avgDurationPrediction.filter(d => d.isPredicted)}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>

            {/* Chart 3: Operations per room */}
            <div className="chart-container">
                <h3>Operations per Room</h3>
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
                <h3>Room Usage Projection</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={roomTrends}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="name" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Bar dataKey="current" fill="#8884d8" name="Current Minutes" />
                        <Bar dataKey="predicted" fill="#82ca9d" name="Predicted Minutes" />
                    </BarChart>
                </ResponsiveContainer>
            </div>
        </div>
        </>
    );
}

export default AnalysisInfo;
