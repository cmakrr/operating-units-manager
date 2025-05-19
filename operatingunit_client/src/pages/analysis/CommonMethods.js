import {message} from "antd";
import { linearRegression, linearRegressionLine } from 'simple-statistics';
import dayjs from "dayjs";

export function validateDates(startDate, endDate){
    if(startDate > endDate){
        message.error(<span>{`Конечная дата анализа должна быть позже начальной!`}</span>);
        return false;
    }
    return true;
}

export function createDateRange(startDate, endDate){
    return {
        "startDate": dayjs(startDate).format('YYYY-MM-DD'),
        "endDate": dayjs(endDate).format('YYYY-MM-DD')
    };
}

export const convertToHours = (minutes) => Math.round(minutes / 60 * 100) / 100;
export const getPredictionValues = (operations) => {

    const predictNextValues = (data, key) => {
        if (data.length === 0) return [];

        const sortedData = [...data].sort((a, b) => new Date(a.date) - new Date(b.date));
        const n = sortedData.length;

        const regression = linearRegression(
            sortedData.map((item, idx) => [idx, item[key]])
        );
        const predict = linearRegressionLine(regression);

        const lastDate = new Date(sortedData[sortedData.length - 1].date);

        return Array.from({ length: n }, (_, i) => {
            const predictionDate = new Date(lastDate);
            predictionDate.setDate(predictionDate.getDate() + i + 1);

            return {
                [key]: Math.max(0, Math.round(predict(sortedData.length + i) * 100) / 100),
                date: predictionDate.toISOString().split('T')[0],
                isPredicted: true
            };
        });
    };

    // 1. Операции по дням
    const opsPerDay = Object.entries(operations).map(([date, ops]) => ({
        date,
        count: ops.length
    }));

    // 2. Средняя продолжительность в часах
    const avgDurationPerDay = Object.entries(operations).map(([date, ops]) => ({
        date,
        avgDuration: ops.length === 0
            ? 0
            : convertToHours(
                ops.reduce((sum, op) => sum + (op.minutesDuration || 0), 0) / ops.length
            )
    }));

    // 3. Статистика по операционным
    const roomCounts = {};
    const roomUsage = {};
    const dailyRoomStats = {};

    Object.entries(operations).forEach(([date, ops]) => {
        // Для ежедневной статистики по комнатам
        const dailyCounts = {};

        ops.forEach(op => {
            // Для общей статистики
            roomCounts[op.operationRoom] = (roomCounts[op.operationRoom] || 0) + 1;

            // Для roomTrends (в минутах)
            if (!roomUsage[op.operationRoom]) {
                roomUsage[op.operationRoom] = {
                    minutes: 0,
                    usageHistory: []
                };
            }
            const duration = op.minutesDuration || 0;
            roomUsage[op.operationRoom].minutes += duration;
            roomUsage[op.operationRoom].usageHistory.push({
                date,
                minutes: duration
            });

            // Для прогноза операций по комнатам
            dailyCounts[op.operationRoom] = (dailyCounts[op.operationRoom] || 0) + 1;
        });

        // Сохраняем ежедневные данные по комнатам
        Object.entries(dailyCounts).forEach(([room, count]) => {
            if (!dailyRoomStats[room]) {
                dailyRoomStats[room] = [];
            }
            dailyRoomStats[room].push({
                date,
                count
            });
        });
    });

    return {
        // Прогноз количества операций по дням
        opsPerDayPrediction: predictNextValues(opsPerDay, 'count'),

        // Прогноз средней продолжительности в часах
        avgDurationPrediction: predictNextValues(avgDurationPerDay, 'avgDuration'),

        // Прогнозируемое количество операций по комнатам
        opsPerRoomPrediction: Object.entries(dailyRoomStats).map(([name, data]) => {
            const predictions = predictNextValues(data, 'count');
            const predictedTotal = predictions.reduce((sum, day) => sum + day.count, 0);
            return {
                name,
                value: Math.max(0, Math.round(predictedTotal)),
                isPredicted: true
            };
        }),

        // Тренды использования операционных (в минутах)
        roomTrends: Object.entries(roomUsage).map(([name, stats]) => {
            const n = Object.keys(operations).length;

            const history = stats.usageHistory
                .sort((a, b) => new Date(a.date) - new Date(b.date))
                .map((entry, idx) => [idx, entry.minutes]);

            const regression = linearRegression(history);
            const predict = linearRegressionLine(regression);

            const predictedMinutes = Array.from({ length: n }, (_, i) =>
                predict(history.length + i)
            ).reduce((sum, minutes) => sum + minutes, 0);

            return {
                name,
                current: convertToHours(stats.minutes) ,
                predicted: Math.max(0, convertToHours(predictedMinutes)),
                predictionPeriod: n
            };
        })
    };
};

export const processOperationsData = (operations) => {
    // 1. Operations per day
    const opsPerDay = Object.entries(operations).map(([date, ops]) => ({
        date,
        count: ops.length
    }));

    // 2. Average duration per day
    const avgDurationPerDay = Object.entries(operations).map(([date, ops]) => {
        const durations = ops.map(op => op.minutesDuration);
        const avg = durations.reduce((a, b) => a + b, 0) / durations.length ;
        return { date, avgDuration: isNaN(avg) ? 0 : Math.round(avg / 60 * 100) / 100};
    });

    // 3. Operations per room
    const roomCounts = {};
    Object.values(operations).forEach(dayOps => {
        dayOps.forEach(op => {
            roomCounts[op.operationRoom] = (roomCounts[op.operationRoom] || 0) + 1;
        });
    });
    const opsPerRoom = Object.entries(roomCounts).map(([room, count]) => ({
        name: room,
        value: count
    }));

    // 4. Total minutes per room
    const roomMinutes = {};
    Object.values(operations).forEach(dayOps => {
        dayOps.forEach(op => {
            roomMinutes[op.operationRoom] = (roomMinutes[op.operationRoom] || 0) + op.minutesDuration;
        });
    });
    const minutesPerRoom = Object.entries(roomMinutes).map(([room, minutes]) => ({
        name: room,
        value: minutes
    }));

    return {
        opsPerDay,
        avgDurationPerDay,
        opsPerRoom,
        minutesPerRoom
    };
};