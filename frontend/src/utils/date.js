import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.locale('zh-cn')
dayjs.extend(relativeTime)

export const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

export const formatRelativeTime = (date) => {
  return dayjs(date).fromNow()
}

export const formatDateShort = (date) => {
  const now = dayjs()
  const target = dayjs(date)
  
  if (now.diff(target, 'day') === 0) {
    return target.format('HH:mm')
  } else if (now.diff(target, 'year') === 0) {
    return target.format('MM-DD')
  } else {
    return target.format('YYYY-MM-DD')
  }
}