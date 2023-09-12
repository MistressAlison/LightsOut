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
uniform int u_lightObjects;
uniform vec4 u_objectXYRI[500];
uniform vec4 u_objectColor[500];
uniform float m_scale;
uniform bool m_mode;
uniform float m_decay;

void main() {
    vec4 outputColor = v_color * texture2D(u_texture, v_texCoords);
    vec4 m_color = vec4(0.99, 0.96, 0.86, 1.0);
    float mouse_r = m_scale*u_scale;
    vec4 c = m_color * max(0.0, 1.0 - distance(u_mouse, gl_FragCoord.xy)/mouse_r);
    if (m_mode) {
        //c = m_color * (mouse_r/pow(distance(u_mouse, gl_FragCoord.xy), m_decay));
        c = m_color * exp(-0.5*pow(distance(u_mouse, gl_FragCoord.xy)*4/mouse_r, m_decay));
    }
    for (int i = 0 ; i < u_lightObjects ; i++) {
        //c += u_objectColor[i] * u_objectXYRI[i].w * (u_objectXYRI[i].z/pow(max(1.0, distance(u_objectXYRI[i].xy, gl_FragCoord.xy)), 1.75f));
        c += u_objectColor[i] * u_objectXYRI[i].w * exp(-0.5*pow(distance(u_objectXYRI[i].xy, gl_FragCoord.xy)/u_objectXYRI[i].z, 2.0f));
    }
    //c.r = min(1.0, c.r);
    //c.g = min(1.0, c.g);
    //c.b = min(1.0, c.b);
    //c.a = min(1.0, c.a);
    gl_FragColor = outputColor * c;
}