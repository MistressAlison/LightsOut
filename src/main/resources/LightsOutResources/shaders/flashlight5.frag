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
uniform vec3 u_XYRIColor[1000];
uniform float m_scale;
uniform bool m_mode;
uniform float m_decay;

float bitShift(float num, int bits) {
    return floor(num / pow(2.0, float(bits)));
}

float bitAnd(float num, int bits) {
    float exp = pow(2.0, float(bits));
    return fract(floor(num)/exp) * exp;
}

vec4 getColor(float colorInt) {
    //uint colorInt = uint(color);
    float r = bitAnd(colorInt, 8) / 255.0;
    float g = bitAnd(bitShift(colorInt, 8), 8) / 255.0;
    float b = bitAnd(bitShift(colorInt, 16), 8) / 255.0;
    float a = bitAnd(bitShift(colorInt, 24), 8) / 255.0;
    //r = colorInt & 255;
    //g = (colorInt >> 8) & 255;
    //b = (colorInt >> 16) & 255;
    //a = (colorInt >> 24) & 255;
    return vec4(r,g,b,a);
}

vec4 getXYRI(float xyInt, float riInt) {
    //uint xyInt = uint(xy);
    //uint riInt = uint(ri);
    float x = bitAnd(xyInt, 16);
    float y = bitAnd(bitShift(xyInt, 16), 16);
    float r = bitAnd(riInt, 16);
    float i = bitAnd(bitShift(riInt, 16), 16) / 1000.0;
    //x = xyInt & 65535;
    //y = (xyInt >> 16) & 65535;
    //r = riInt & 65535;
    //i = ((riInt >> 16) & 65535) * 1000.0;
    return vec4(x,y,r,i);
}

void main() {
    vec4 outputColor = v_color * texture2D(u_texture, v_texCoords);
    vec4 m_color = vec4(0.99, 0.96, 0.86, 1.0);
    float mouse_r = m_scale*u_scale;
    vec4 c = m_color * max(0.0, 1.0 - distance(u_mouse, gl_FragCoord.xy)/mouse_r);
    if (m_mode) {
        //c = m_color * (mouse_r/pow(distance(u_mouse, gl_FragCoord.xy), m_decay));
        c = m_color * exp(-0.5*pow(distance(u_mouse, gl_FragCoord.xy)*4.0/mouse_r, m_decay));
    }
    for (int i = 0 ; i < u_lightObjects ; i++) {
        vec4 objectXYRI = getXYRI(u_XYRIColor[i].x, u_XYRIColor[i].y);
        vec4 objectColor = getColor(u_XYRIColor[i].z);
        c += objectColor * objectXYRI.w * exp(-0.5*pow(distance(objectXYRI.xy, gl_FragCoord.xy)/objectXYRI.z, 2.0));
    }
    //c.r = min(1.0, c.r);
    //c.g = min(1.0, c.g);
    //c.b = min(1.0, c.b);
    //c.a = min(1.0, c.a);
    gl_FragColor = outputColor * c;
}