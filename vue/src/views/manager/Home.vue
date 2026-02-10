<template>
    <div className="home-container">
        <div style="font-weight: bold; font-size: 20px; margin-bottom: 20px">数据统计概览</div>

        <!-- 左右布局容器 -->
        <div style="display: flex; gap: 20px;">

            <!-- 左侧：宿舍购买力 -->
            <div className="card" style="flex: 1;">
                <div className="chart-title">宿舍购买力排行 (辅助配送策略)</div>
                <div id="pieChart" style="width: 100%; height: 400px;"></div>
            </div>

            <!-- 右侧：热销商品 Top 10 -->
            <div className="card" style="flex: 1;">
                <div className="chart-title">热销商品 Top 10 (辅助进货决策)</div>
                <div id="barChart" style="width: 100%; height: 400px;"></div>
            </div>

        </div>
    </div>
</template>

<script setup>
import {onMounted, reactive} from "vue";
import * as echarts from "echarts";
import request from "@/utils/request";

// 页面加载完成后初始化
onMounted(() => {
    loadPurchasePowerData();
    loadTop10Data();
})

// --- 左侧：宿舍购买力逻辑 ---
const loadPurchasePowerData = () => {
    request.get('/orders/purchase-power').then(res => {
        if (res.code === '200') {
            initPieChart(res.data);
        } else {
            console.error("加载购买力数据失败", res.msg);
        }
    })
}

const initPieChart = (data) => {
    const chartDom = document.getElementById('pieChart');
    const myChart = echarts.init(chartDom);
    const option = {
        tooltip: {
            trigger: 'item',
            formatter: '{b} : {c}元 ({d}%)'
        },
        legend: {
            orient: 'vertical',
            left: 'left'
        },
        series: [
            {
                name: '消费总额',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: data, // 后端返回 {name: '南区', value: 1000}
                emphasis: {
                    itemStyle: {shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)'}
                }
            }
        ]
    };
    myChart.setOption(option);
}

// --- 右侧：热销 Top 10 逻辑 ---
const loadTop10Data = () => {
    request.get('/goods/selectTop10').then(res => {
        if (res.code === '200') {
            // 数据处理：提取名称和销量
            const names = res.data.map(item => item.name);
            const values = res.data.map(item => item.saleCount);
            initBarChart(names, values);
        } else {
            console.error("加载Top10数据失败", res.msg);
        }
    })
}

const initBarChart = (names, values) => {
    const chartDom = document.getElementById('barChart');
    const myChart = echarts.init(chartDom);
    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {type: 'shadow'}
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value',
            name: '销量'
        },
        yAxis: {
            type: 'category',
            data: names,
            inverse: true, // 反转坐标轴，让第一名显示在最上面
            axisLabel: {
                width: 100, // 限制名称宽度
                overflow: 'truncate' // 超长省略
            }
        },
        series: [
            {
                name: '销量',
                type: 'bar',
                data: values,
                itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                        {offset: 0, color: '#83bff6'},
                        {offset: 0.5, color: '#188df0'},
                        {offset: 1, color: '#188df0'}
                    ])
                },
                label: {
                    show: true,
                    position: 'right'
                }
            }
        ]
    };
    myChart.setOption(option);
}
</script>

<style scoped>
.home-container {
    padding: 20px;
}

.card {
    background-color: #fff;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart-title {
    font-weight: bold;
    font-size: 16px;
    color: #333;
    margin-bottom: 15px;
    border-left: 4px solid #409EFF;
    padding-left: 10px;
}
</style>