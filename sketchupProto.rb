$ox1 = 10
$oy1=10
$ox2=50
$oy2=50

def f(x,y)
 return [x*10,y*10,(Math.cos(Math.sqrt(($ox1-x)*($ox1-x)+($oy1-y)*($oy1-y))/5*2*3.14)*5*(40/Math.sqrt(($ox1-x)*($ox1-x)+($oy1-y)*($oy1-y)))+Math.cos(Math.sqrt(($ox2-x)*($ox2-x)+($oy2-y)*($oy2-y))/5*2*3.14)*2*(40/Math.sqrt(($ox2-x)*($ox2-x)+($oy2-y)*($oy2-y))))/2]
end


(1..100).each do |j|
(1..100).each do |i|
add_face_group [f(i,j),f(i+1,j),f(i,j+1)]
add_face_group [f(i+1,j),f(i+1,j+1),f(i,j+1)]
end
end



grad = 2*3.14/360
s = Array.new()
s.push(Array.new([0,0,0]))
(1..5).each do |j|
r = 10*j
(0..j*35).each do |i|
s.push(Array.new([r*Math.sin(i*grad*10),r*Math.cos(i*grad*10),j*10]))
# s.push(Array.new([r*Math.sin((i+1)*grad*10),r*Math.cos((i+1)*grad*10),0]))
end
end
add_face_group(s)





