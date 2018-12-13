<template>
    <el-menu :default-active="$route.path" background-color="#001529" active-text-color="#ffd04b"
                :router=true  :unique-opened=true>
        <el-submenu v-for="(item,index) in menuData" :key="index" :index="item.title" v-if="item.url ===''">
            <template slot="title">
                {{item.title}}
            </template>
            <el-menu-item v-for="(entity) in item.child" :index="entity.url">
                {{entity.title}}
            </el-menu-item>
        </el-submenu>
        <el-menu-item v-else :index="item.url" :name="item.url">{{item.title}}</el-menu-item>
    </el-menu>
</template>
<script>
  export default {
    props: ['menuData', 'menu'],
    data() {
      return {}
    }
  }
</script>
