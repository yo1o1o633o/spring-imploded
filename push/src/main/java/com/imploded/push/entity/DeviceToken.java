package com.imploded.push.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author fandy.lin
 */
@Data
@Entity
@Table(name = "push_device_token")
public class DeviceToken {
    /**
     * 自增id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    /**
     * 应用ID
     */
    @Column(name = "push_app_id")
    private Integer pushAppId;

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 设备ID
     */
    @Column(name = "device_token")
    private String deviceToken;

    /**
     * 通道。极光：0；苹果：1;华为：2；小米:3；OPPO：4；VIVO:5; 魅族：6
     */
    private Byte channel;

    /**
     * 操作系统。ios：1; android：0
     */
    private Byte platform;

    /**
     * 土巴兔App唯一设备id
     */
    @Column(name = "first_id")
    private String firstId;

    /**
     * IOS厂商设备号
     */
    @Column(name = "provide_device_token")
    private String provideDeviceToken;

    /**
     * ANDROID厂商设备号
     */
    @Column(name = "reg_id")
    private String regId;

    /**
     * app未读角标值
     */
    private Integer badge;

    /**
     * 版本号
     */
    @Column(name = "version")
    private String version;

    /**
     * 是否删除
     */
    private Byte deleted;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Integer updateTime;

    /**
     * 是否积分墙
     */
    @Column(name = "integral_wall")
    private Integer integralWall;
}