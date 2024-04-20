#include <px4_platform_common/px4_config.h>
#include <px4_platform_common/log.h>
#include <drivers/drv_hrt.h>
#include <uORB/Publication.hpp>
#include <uORB/topics/rc_channels.h>
#include <uORB/topics/test_motor.h>
#include <uORB/topics/led_control.h>
#include <uORB/topics/sensor_combined.h>
#include "math.h"

#define DC_MOTOR 0
#define SERVO_MOTOR 1

extern "C" __EXPORT int hello_world_main(int argc, char *argv[]);

int hello_world_main(int argc, char *argv[]) {
    int rc_channel_handle;
    rc_channels_s rc_data;
    rc_channel_handle = orb_subscribe(ORB_ID(rc_channels));
    orb_set_interval(rc_channel_handle, 200);

    uORB::Publication<test_motor_s> test_motor_pub(ORB_ID(test_motor));
    test_motor_s test_motor;
    
    
    uORB::Publication<led_control_s> led_control_pub(ORB_ID(led_control));
    led_control_s led_control;
    
    int sensor_combined_handle;
    sensor_combined_s sensor_data;
    sensor_combined_handle = orb_subscribe(ORB_ID(sensor_combined));
    orb_set_interval(sensor_combined_handle, 200);


    while (1) {
        orb_copy(ORB_ID(rc_channels), rc_channel_handle, &rc_data);
        orb_copy(ORB_ID(sensor_combined), sensor_combined_handle, &sensor_data);

        float motor_value = 0.0;
        float servo_value = 1 - ((double)rc_data.channels[0] + 1) / 2; // Map [-1, 1] to [0, 1]

        int direction = ((double)rc_data.channels[4] > 0.5) ? 1 : -1;

        double speed_limit = 1.0;
        int led_color = 1;
        if ((double)rc_data.channels[5] > 0.5) { // High speed mode
            speed_limit = 1.0;
            led_color = 1; //red for fast mode
        }else if ((double)rc_data.channels[5] < -0.5) { // Low speed mode
            speed_limit = 1.0 / 3.0;
            led_color = 4; //yellow
        } else { // Medium speed mode
            speed_limit = 2.0 / 3.0;
            led_color = 5; //purple
        }
        
        // accelerometer
        double xAngle = (-1) * (double)sensor_data.accelerometer_m_s2[0] * (90/11) / 180 * 3.1415926; //in radians
        PX4_INFO("angle %.2f %.2f\n", xAngle, (double)sensor_data.accelerometer_m_s2[0] );
        double acceMaxSpeed = 0.1;

        double joystick_value = rc_data.channels[2];

        if (direction == 1) { // Forward
            motor_value = 0.5 + ((joystick_value + 1) / 2) * (0.5 * speed_limit) + acceMaxSpeed*sin(xAngle);
            if (motor_value<0.51f) motor_value = 0.51;
            else if (motor_value>1.0f) motor_value = 1;
        } else { // Reverse
            motor_value = 0.5 - ((joystick_value + 1) / 2) * (0.5 * speed_limit) + acceMaxSpeed*sin(xAngle);
            if (motor_value>0.5f) motor_value = 0.5;
            else if (motor_value<0.0f) motor_value = 0;
        }

        test_motor.timestamp = hrt_absolute_time();
        test_motor.motor_number = DC_MOTOR;
        test_motor.value = motor_value;
        test_motor.action = test_motor_s::ACTION_RUN;
        test_motor.driver_instance = 0;
        test_motor.timeout_ms = 0;
        test_motor_pub.publish(test_motor);

        test_motor.timestamp = hrt_absolute_time();
        test_motor.motor_number = SERVO_MOTOR;
        test_motor.value = servo_value;
        test_motor.action = test_motor_s::ACTION_RUN;
        test_motor.driver_instance = 0;
        test_motor.timeout_ms = 0;
        test_motor_pub.publish(test_motor);
        
        led_control.timestamp = hrt_absolute_time();
        led_control.led_mask = 0xFF;
        led_control.color = led_color;
        led_control.mode = 1; //on
        led_control.num_blinks = 0;
        led_control.priority = led_control_s::MAX_PRIORITY;
        led_control_pub.publish(led_control);
        
        PX4_INFO("val %.2f %.2f %d\n", (double)motor_value, (double)servo_value, led_color);
        
        //PX4_INFO("X = %f, Y = %f, Z = %f", (double)sensor_data.accelerometer_m_s2[0], (double)sensor_data.accelerometer_m_s2[1], (double)sensor_data.accelerometer_m_s2[2]);

        px4_usleep(200000); // Loop at 5Hz
    }
    return 0;
}
