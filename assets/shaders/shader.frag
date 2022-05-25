#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform float u_time;

uniform vec2 u_resolution;  // Canvas size (width,height)
uniform vec2 u_mouse;       // mouse position in screen pixels

const float line_gap = 1.0; // px
// line_size is visible height
const float line_size = 4.0; // px

// parameters of ellipse
const float a = .6, b = .8;

// function for laggy, noisy shift of image
float noise(float x) {
    return (sin(3.0*x)
    + cos(8.0*x)
    - cos(3.0 + 14.0 * x)
    - sin(x * x / 2.0)
    ) / 4.0;
}

vec3 dithering(vec2 uv)
{
    int positionX = int(uv.x * u_resolution.x);
    int positionY = int(uv.y * u_resolution.y);

    vec3 inC = texture2D(u_texture, uv).rgb;

    vec3 index = inC * 4.999999f;

    int oddX = positionX & 1;
    int oddY = positionY & 1;

    int val[5];
    val[0] = 0;           // 0.0f <= x < 0.2f
    val[1] = oddX & oddY; // 0.2f <= x < 0.4f
    val[2] = oddX ^ oddY; // 0.4f <= x < 0.6f
    val[3] = oddX | oddY; // 0.6f <= x < 0.8f
    val[4] = 1;           // 0.8f <= x

    //return vec3(val[1], val[2], val[3]);

    return vec3(val[int(index.r)], val[int(index.g)], val[int(index.b)]);
}

void main(){

/*    vec2 res = vec2(480, 800);
    vec2 st = gl_FragCoord.xy/u_resolution;
    gl_FragColor = vec4(st.x,st.y,0.0,0.2);*/

    //gl_FragColor = vec4(vec3(1.0), 1.0);
    //gl_FragColor = vec4(abs(sin(u_time)),0.0,0.0,0.1);
    //gl_FragColor.rgb=gl_FragColor.rgb;
    /*vec3 color = texture2D(u_texture, v_texCoords).rgb;
    float gray = (color.r + color.g + color.b) / 3.0;
    vec3 grayscale = vec3(gray);

    gl_FragColor = gl_FragColor;*/

/*    vec2 uv = 2.0 * gl_FragCoord/ u_resolution.xy - 1.0;

    float len = 1.0 - (pow(abs(uv.x), 9.0) / a + pow(abs(uv.y), 3.0) / b);

    // uncomment for visible edges
    // len = step(0.0, len);

    if (len > 0.0) {
        vec4 intensity = vec4(len, len, len, 1.0);

        vec2 skew = vec2(0.0);
        skew.x = smoothstep(0.7, 1.0, (u_time)) / 10.0;

        float modulo = line_gap + line_size;
        if (mod(gl_FragCoord.y + modulo / 2.0, modulo) >= line_gap) {
            gl_FragColor = texture2D(u_texture, (uv + vec2(1.0))/2.0 + skew);
        } else {
            //gl_FragColor = vec4(vec3(1.0), 0.5);
        }
    }else{
        gl_FragColor = vec4(vec3(0.0), 0.1);
    }*/

/*    vec2 uv = gl_FragCoord.xy / u_resolution.xy;

    uv *=  1.0 - uv.yx;   //vec2(1.0)- uv.yx; -> 1.-u.yx; Thanks FabriceNeyret !

    float vig = uv.x*uv.y * 15.0; // multiply with sth for intensity

    vig = pow(vig, 0.25); // change pow for modifying the extend of the  vignette


    gl_FragColor = vec4(vig);*/


    vec4 color;

    vec2 uv = gl_FragCoord.xy / u_resolution.xy;

    float distanceFromCenter = length( uv - vec2(0.5,0.5) );

    float vignetteAmount;

    float lum;

    vignetteAmount = 1.0 - distanceFromCenter;
    vignetteAmount = smoothstep(0.1, 0.3, vignetteAmount / 4);

    color = texture2D( u_texture, uv);

    // luminance hack, responses to red channel most
    lum = dot(color.rgb, vec3( 0.85, 0.30, 0.10) );

    //color.rgb = vec3(0.0, lum, 0.0);

    // scanlines
    color += 0.1*sin(uv.y*u_resolution.y*2.0);

    // screen flicker
    color += 0.005 * sin(u_time*16.0);

    // vignetting
    color *=  vignetteAmount*1.0;
    color.a = 0.25;
    gl_FragColor = color;

}