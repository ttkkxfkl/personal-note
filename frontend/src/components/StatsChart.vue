<template>
  <div class="stats-chart-container">
    <!-- 统计概览卡片 -->
    <div class="stats-overview">
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Document /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.totalNotes }}</h3>
          <p>总笔记数</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Folder /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.totalNotebooks }}</h3>
          <p>笔记本数</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><PriceTag /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ stats.totalTags }}</h3>
          <p>标签数</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><EditPen /></el-icon>
        </div>
        <div class="stat-content">
          <h3>{{ formatNumber(stats.totalWords) }}</h3>
          <p>总字数</p>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <!-- 笔记创建趋势 -->
      <div class="chart-card">
        <div class="chart-header">
          <h4>笔记创建趋势</h4>
          <el-radio-group v-model="trendPeriod" size="small" @change="updateTrendChart">
            <el-radio-button label="7d">7天</el-radio-button>
            <el-radio-button label="30d">30天</el-radio-button>
            <el-radio-button label="90d">90天</el-radio-button>
          </el-radio-group>
        </div>
        <div class="chart-content">
          <v-chart 
            ref="trendChart" 
            :option="trendChartOption" 
            :loading="loading.trend"
            style="height: 280px"
          />
        </div>
      </div>

      <!-- 笔记本分布 -->
      <div class="chart-card">
        <div class="chart-header">
          <h4>笔记本分布</h4>
        </div>
        <div class="chart-content">
          <v-chart 
            ref="notebookChart" 
            :option="notebookChartOption" 
            :loading="loading.notebook"
            style="height: 280px"
          />
        </div>
      </div>

      <!-- 标签使用统计 -->
      <div class="chart-card">
        <div class="chart-header">
          <h4>热门标签</h4>
        </div>
        <div class="chart-content">
          <v-chart 
            ref="tagChart" 
            :option="tagChartOption" 
            :loading="loading.tag"
            style="height: 280px"
          />
        </div>
      </div>

      <!-- 写作活跃度热力图 -->
      <div class="chart-card full-width">
        <div class="chart-header">
          <h4>写作活跃度热力图</h4>
          <el-select v-model="heatmapYear" size="small" @change="updateHeatmapChart">
            <el-option 
              v-for="year in availableYears" 
              :key="year" 
              :label="year + '年'" 
              :value="year" 
            />
          </el-select>
        </div>
        <div class="chart-content">
          <v-chart 
            ref="heatmapChart" 
            :option="heatmapChartOption" 
            :loading="loading.heatmap"
            style="height: 200px"
          />
        </div>
      </div>
    </div>

    <!-- 详细统计表格 -->
    <div class="detailed-stats">
      <el-tabs v-model="activeStatsTab" class="stats-tabs">
        <el-tab-pane label="最近活动" name="activity">
          <div class="activity-list">
            <div 
              v-for="activity in recentActivities" 
              :key="activity.id"
              class="activity-item"
            >
              <div class="activity-icon">
                <el-icon>
                  <component :is="getActivityIcon(activity.type)" />
                </el-icon>
              </div>
              <div class="activity-content">
                <p class="activity-description">{{ activity.description }}</p>
                <span class="activity-time">{{ formatTime(activity.time) }}</span>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="笔记统计" name="notes">
          <el-table :data="noteStats" style="width: 100%">
            <el-table-column prop="notebook" label="笔记本" />
            <el-table-column prop="count" label="笔记数量" />
            <el-table-column prop="words" label="总字数" />
            <el-table-column prop="lastUpdated" label="最后更新" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="标签统计" name="tags">
          <el-table :data="tagStats" style="width: 100%">
            <el-table-column prop="name" label="标签名称" />
            <el-table-column prop="count" label="使用次数" />
            <el-table-column prop="percentage" label="占比" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart, BarChart, HeatmapChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  CalendarComponent,
  VisualMapComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import dayjs from 'dayjs'

// 注册 ECharts 组件
use([
  CanvasRenderer,
  LineChart,
  PieChart,
  BarChart,
  HeatmapChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  CalendarComponent,
  VisualMapComponent
])

const emit = defineEmits(['export-stats'])

// 响应式数据
const stats = reactive({
  totalNotes: 0,
  totalNotebooks: 0,
  totalTags: 0,
  totalWords: 0
})

const loading = reactive({
  trend: false,
  notebook: false,
  tag: false,
  heatmap: false
})

const trendPeriod = ref('30d')
const heatmapYear = ref(new Date().getFullYear())
const activeStatsTab = ref('activity')

const trendChartOption = ref({})
const notebookChartOption = ref({})
const tagChartOption = ref({})
const heatmapChartOption = ref({})

const recentActivities = ref([])
const noteStats = ref([])
const tagStats = ref([])

// 计算属性
const availableYears = computed(() => {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 3 }, (_, i) => currentYear - i)
})

// 方法
const formatNumber = (num) => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'k'
  }
  return num.toString()
}

const formatTime = (time) => {
  return dayjs(time).format('MM-DD HH:mm')
}

const getActivityIcon = (type) => {
  const iconMap = {
    'create': 'DocumentAdd',
    'update': 'Edit',
    'delete': 'Delete',
    'favorite': 'StarFilled',
    'tag': 'PriceTag'
  }
  return iconMap[type] || 'Document'
}

const loadStats = async () => {
  try {
    // 模拟数据，实际项目中从API获取
    stats.totalNotes = 156
    stats.totalNotebooks = 12
    stats.totalTags = 45
    stats.totalWords = 89567

    // 加载活动记录
    recentActivities.value = [
      {
        id: 1,
        type: 'create',
        description: '创建了新笔记「Vue 3 响应式原理」',
        time: new Date(Date.now() - 1000 * 60 * 30) // 30分钟前
      },
      {
        id: 2,
        type: 'update',
        description: '更新了笔记「JavaScript 闭包详解」',
        time: new Date(Date.now() - 1000 * 60 * 120) // 2小时前
      },
      {
        id: 3,
        type: 'favorite',
        description: '收藏了笔记「设计模式总结」',
        time: new Date(Date.now() - 1000 * 60 * 60 * 6) // 6小时前
      }
    ]

    // 笔记统计
    noteStats.value = [
      { notebook: '前端开发', count: 45, words: 23456, lastUpdated: '2024-01-15' },
      { notebook: '算法学习', count: 32, words: 18765, lastUpdated: '2024-01-14' },
      { notebook: '项目笔记', count: 28, words: 15432, lastUpdated: '2024-01-13' },
      { notebook: '读书笔记', count: 51, words: 31914, lastUpdated: '2024-01-12' }
    ]

    // 标签统计
    tagStats.value = [
      { name: 'JavaScript', count: 34, percentage: '21.8%' },
      { name: 'Vue', count: 28, percentage: '17.9%' },
      { name: 'React', count: 22, percentage: '14.1%' },
      { name: '算法', count: 19, percentage: '12.2%' },
      { name: 'Node.js', count: 15, percentage: '9.6%' }
    ]
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const updateTrendChart = async () => {
  loading.trend = true
  try {
    // 模拟趋势数据
    const days = trendPeriod.value === '7d' ? 7 : trendPeriod.value === '30d' ? 30 : 90
    const data = []
    const categories = []
    
    for (let i = days - 1; i >= 0; i--) {
      const date = dayjs().subtract(i, 'day')
      categories.push(date.format('MM-DD'))
      data.push(Math.floor(Math.random() * 10) + 1)
    }

    trendChartOption.value = {
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: categories,
        boundaryGap: false
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        name: '笔记数量',
        type: 'line',
        data: data,
        smooth: true,
        areaStyle: {
          opacity: 0.3
        },
        itemStyle: {
          color: '#409EFF'
        }
      }]
    }
  } finally {
    loading.trend = false
  }
}

const updateNotebookChart = async () => {
  loading.notebook = true
  try {
    notebookChartOption.value = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [{
        name: '笔记分布',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 45, name: '前端开发' },
          { value: 32, name: '算法学习' },
          { value: 28, name: '项目笔记' },
          { value: 51, name: '读书笔记' }
        ]
      }]
    }
  } finally {
    loading.notebook = false
  }
}

const updateTagChart = async () => {
  loading.tag = true
  try {
    tagChartOption.value = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'category',
        data: ['JavaScript', 'Vue', 'React', '算法', 'Node.js']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        name: '使用次数',
        type: 'bar',
        data: [34, 28, 22, 19, 15],
        itemStyle: {
          color: '#67C23A'
        }
      }]
    }
  } finally {
    loading.tag = false
  }
}

const updateHeatmapChart = async () => {
  loading.heatmap = true
  try {
    // 生成一年的热力图数据
    const data = []
    const startDate = dayjs(`${heatmapYear.value}-01-01`)
    
    for (let i = 0; i < 365; i++) {
      const date = startDate.add(i, 'day')
      const value = Math.floor(Math.random() * 10)
      data.push([date.format('YYYY-MM-DD'), value])
    }

    heatmapChartOption.value = {
      tooltip: {
        formatter: function (params) {
          return `${params.data[0]}: ${params.data[1]} 篇笔记`
        }
      },
      visualMap: {
        min: 0,
        max: 10,
        type: 'piecewise',
        orient: 'horizontal',
        left: 'center',
        top: 20,
        inRange: {
          color: ['#ebedf0', '#c6e48b', '#7bc96f', '#239a3b', '#196127']
        }
      },
      calendar: {
        top: 60,
        left: 30,
        right: 30,
        cellSize: ['auto', 13],
        range: heatmapYear.value,
        itemStyle: {
          borderWidth: 0.5
        },
        yearLabel: { show: false }
      },
      series: [{
        type: 'heatmap',
        coordinateSystem: 'calendar',
        data: data
      }]
    }
  } finally {
    loading.heatmap = false
  }
}

// 生命周期
onMounted(async () => {
  await loadStats()
  await Promise.all([
    updateTrendChart(),
    updateNotebookChart(),
    updateTagChart(),
    updateHeatmapChart()
  ])
})
</script>

<style scoped>
.stats-chart-container {
  padding: 20px;
  background: var(--bg-color-primary);
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: var(--bg-color-secondary);
  border-radius: 12px;
  border: 1px solid var(--border-color);
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  margin-right: 16px;
  border-radius: 12px;
  background: var(--el-color-primary-light-9);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--el-color-primary);
  font-size: 24px;
}

.stat-content h3 {
  margin: 0 0 4px 0;
  font-size: 24px;
  font-weight: 600;
  color: var(--text-color-primary);
}

.stat-content p {
  margin: 0;
  font-size: 14px;
  color: var(--text-color-secondary);
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.chart-card {
  background: var(--bg-color-secondary);
  border-radius: 12px;
  border: 1px solid var(--border-color);
  overflow: hidden;
}

.chart-card.full-width {
  grid-column: 1 / -1;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-color);
  background: var(--bg-color-primary);
}

.chart-header h4 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-color-primary);
}

.chart-content {
  padding: 20px;
}

.detailed-stats {
  background: var(--bg-color-secondary);
  border-radius: 12px;
  border: 1px solid var(--border-color);
  overflow: hidden;
}

.stats-tabs {
  padding: 0 20px;
}

.activity-list {
  max-height: 400px;
  overflow-y: auto;
  padding: 20px;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  padding: 12px 0;
  border-bottom: 1px solid var(--border-color-lighter);
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 32px;
  height: 32px;
  margin-right: 12px;
  border-radius: 8px;
  background: var(--el-color-info-light-8);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--el-color-info);
  font-size: 16px;
  flex-shrink: 0;
}

.activity-content {
  flex: 1;
}

.activity-description {
  margin: 0 0 4px 0;
  font-size: 14px;
  color: var(--text-color-primary);
}

.activity-time {
  font-size: 12px;
  color: var(--text-color-secondary);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .stats-overview {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .charts-container {
    grid-template-columns: 1fr;
  }
  
  .chart-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}

@media (max-width: 480px) {
  .stats-overview {
    grid-template-columns: 1fr;
  }
  
  .stat-card {
    flex-direction: column;
    text-align: center;
  }
  
  .stat-icon {
    margin: 0 0 12px 0;
  }
}
</style>