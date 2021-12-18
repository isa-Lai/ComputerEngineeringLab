% read img
img = imread('ee3tp3picture2021.jpg');
img_r = img(:,:,1);
img_g = img(:,:,2);
img_b = img(:,:,3);

% refine rgb
img_r = img_r.*3;
img_g = img_g.*1.3;

% export
re_img = cat(3,img_r,img_g, img_b);
imshow(re_img);
imwrite(re_img,'my_fixed_image.jpg');