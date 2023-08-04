#ifdef GL_ES
#define LOWP
precision mediump float;
#else
#define LOWP
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
//varying vec4 v_pos;
//varying vec4 v_apos;

uniform sampler2D u_texture;
uniform float u_scale;//settings dot scale
uniform vec2 u_screenSize;//width, height
uniform vec2 u_mouse;
uniform float x_time;
uniform int torches;
uniform vec2 u_torch[32];
uniform float m_scale;
uniform float t_scale;
uniform bool greenTorch;

void main() {
    //d needs to be a vec4 as well to handle each color channel
    vec4 m_color = vec4(0.99, 0.96, 0.86, 1.0);
    //m_color = vec4(1.0, 0.5, 0.5, 1.0);
    vec4 t_color = greenTorch ? vec4(0.5f, 1.0f, 0.5f, 1.0f) : vec4(1.0, 1.0, 0.0, 1.0);
    //t_color = vec4(1.0, 1.0, 1.0, 1.0);
    vec4 outputColor = v_color * texture2D(u_texture, v_texCoords);
    float mouse_r = m_scale*min(u_screenSize.x, u_screenSize.y)/3;
    float torch_r = t_scale*min(u_screenSize.x, u_screenSize.y)/5;
    float d = max(0.0, 1.0 - distance(u_mouse, gl_FragCoord.xy)/mouse_r);
    vec4 c = m_color * max(0.0, 1.0 - distance(u_mouse, gl_FragCoord.xy)/mouse_r);
    for (int i = 0 ; i < 32 ; i++) {
        if (i < torches) {
            d += max(0.0, 1.0 - distance(u_torch[i].xy, gl_FragCoord.xy)/torch_r);
            c += t_color * max(0.0, 1.0 - distance(u_torch[i].xy, gl_FragCoord.xy)/torch_r);
        }
    }
    float fd = 1 - min(1.0f, d);
    float fa = min(1.0f, d);
    //c.r = min(1.0, c.r);
    //c.g = min(1.0, c.g);
    //c.b = min(1.0, c.b);
    //c.a = min(1.0, c.a);
    //gl_FragColor = vec4(outputColor.r*fa, outputColor.g*fa, outputColor.b*fa, outputColor.a*fa);
    gl_FragColor = outputColor * c;
}