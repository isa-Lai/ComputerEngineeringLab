#include <px4_platform_common/px4_config.h>
#include <px4_platform_common/log.h>

#include <drivers/drv_hrt.h>
#include <uORB/Publication.hpp>
#include <uORB/topics/test_motor.h>
#include <uORB/topics/debug_value.h>
#include <math.h>

#define DC_MOTOR 0
#define SERVO_MOTOR 1

extern "C" __EXPORT int hello_world_main(int argc, char *argv[]);

void publish_motor(test_motor_s *, float, int, uORB::Publication<test_motor_s> *);

int hello_world_main(int argc, char *argv[])
{
	px4_sleep(2);
	debug_value_s debug_data;
	int debug_handle = orb_subscribe(ORB_ID(debug_value));
	orb_set_interval(debug_handle, 500);

	test_motor_s test_dc;
	test_motor_s test_servo;
	uORB::Publication<test_motor_s> test_dc_pub(ORB_ID(test_motor));
	uORB::Publication<test_motor_s> test_servo_pub(ORB_ID(test_motor));

	float dc_value = 0.5; // a number between 0 to 1
	float servo_value = 0.5;
	const float max_dc_value = 0.6;


	//init motor
	publish_motor(&test_dc, 0.5, dc_value, &test_dc_pub);
	publish_motor(&test_servo, servo_value, SERVO_MOTOR, &test_servo_pub);

	while (1)
	{
		orb_copy(ORB_ID(debug_value), debug_handle, &debug_data);

		// serveo
		if (debug_data.ind == 0)
		{
			servo_value = 0.8;
		}
		else if (debug_data.ind == 1)
		{
			servo_value = 0.5;
		}
		else if (debug_data.ind == 2)
		{
			servo_value = 0.3;
		}
		publish_motor(&test_servo, servo_value, SERVO_MOTOR, &test_servo_pub);

		// dc
		if (debug_data.value < 30.0f && debug_data.value >5.0f)
		{
			dc_value = 0.5f + (max_dc_value-0.5f)*debug_data.value/30.0f;
		}
		else if (debug_data.value <= 5.0f)
		{
			dc_value = 0.5; //stop
		}
		else
		{
			dc_value = max_dc_value;
		}
		publish_motor(&test_dc, dc_value, DC_MOTOR, &test_dc_pub);

		px4_usleep(200000);
	}
	return 0;
}

void publish_motor(test_motor_s *test_motor, float motor_value, int motor_number, uORB::Publication<test_motor_s> *test_motor_pub)
{
	test_motor->timestamp = hrt_absolute_time();
	test_motor->motor_number = motor_number;
	test_motor->value = motor_value;
	test_motor->action = test_motor_s::ACTION_RUN;
	test_motor->driver_instance = 0;
	test_motor->timeout_ms = 0;
	test_motor_pub->publish(*test_motor);
}

