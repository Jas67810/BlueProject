//
//  QMUIAlbumViewController.m
//  qmui
//
//  Created by Kayo Lee on 15/5/3.
//  Copyright (c) 2015年 QMUI Team. All rights reserved.
//

#import "QMUIAlbumViewController.h"
#import "QMUICore.h"
#import "QMUINavigationButton.h"
#import "UIView+QMUI.h"
#import "QMUIAssetsManager.h"
#import "QMUIImagePickerViewController.h"
#import "QMUIImagePickerHelper.h"
#import <Photos/PHPhotoLibrary.h>
#import <Photos/PHAsset.h>
#import <Photos/PHFetchOptions.h>
#import <Photos/PHCollection.h>
#import <Photos/PHFetchResult.h>

#pragma mark - QMUIAlbumTableViewCell

@implementation QMUIAlbumTableViewCell

+ (void)initialize {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        [QMUIAlbumTableViewCell appearance].albumImageSize = 72;
        [QMUIAlbumTableViewCell appearance].albumImageMarginLeft = 16;
        [QMUIAlbumTableViewCell appearance].albumNameInsets = UIEdgeInsetsMake(0, 14, 0, 3);
        [QMUIAlbumTableViewCell appearance].albumNameFont = UIFontMake(17);
        [QMUIAlbumTableViewCell appearance].albumNameColor = TableViewCellTitleLabelColor;
        [QMUIAlbumTableViewCell appearance].albumAssetsNumberFont = UIFontMake(17);
        [QMUIAlbumTableViewCell appearance].albumAssetsNumberColor = TableViewCellTitleLabelColor;
    });
}

- (void)didInitializeWithStyle:(UITableViewCellStyle)style {
    [super didInitializeWithStyle:style];
    self.albumImageSize = [QMUIAlbumTableViewCell appearance].albumImageSize;
    self.albumImageMarginLeft = [QMUIAlbumTableViewCell appearance].albumImageMarginLeft;
    self.albumNameInsets = [QMUIAlbumTableViewCell appearance].albumNameInsets;
    self.albumNameFont = [QMUIAlbumTableViewCell appearance].albumNameFont;
    self.albumNameColor = [QMUIAlbumTableViewCell appearance].albumNameColor;
    self.albumAssetsNumberFont = [QMUIAlbumTableViewCell appearance].albumAssetsNumberFont;
    self.albumAssetsNumberColor = [QMUIAlbumTableViewCell appearance].albumAssetsNumberColor;
    
    self.imageView.contentMode = UIViewContentModeScaleAspectFill;
    self.imageView.clipsToBounds = YES;
    self.imageView.layer.borderWidth = PixelOne;
    self.imageView.layer.borderColor = UIColorMakeWithRGBA(0, 0, 0, .1).CGColor;
}

- (void)updateCellAppearanceWithIndexPath:(NSIndexPath *)indexPath {
    [super updateCellAppearanceWithIndexPath:indexPath];
    self.textLabel.font = self.albumNameFont;
    self.detailTextLabel.font = self.albumAssetsNumberFont;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    CGFloat imageEdgeTop = CGFloatGetCenter(CGRectGetHeight(self.contentView.bounds), self.albumImageSize);
    CGFloat imageEdgeLeft = self.albumImageMarginLeft == -1 ? imageEdgeTop : self.albumImageMarginLeft;
    self.imageView.frame = CGRectMake(imageEdgeLeft, imageEdgeTop, self.albumImageSize, self.albumImageSize);
    
    self.textLabel.frame = CGRectSetXY(self.textLabel.frame, CGRectGetMaxX(self.imageView.frame) + self.albumNameInsets.left, [self.textLabel qmui_topWhenCenterInSuperview]);
    
    CGFloat textLabelMaxWidth = CGRectGetWidth(self.contentView.bounds) - CGRectGetMinX(self.textLabel.frame) - CGRectGetWidth(self.detailTextLabel.bounds) - self.albumNameInsets.right;
    if (CGRectGetWidth(self.textLabel.bounds) > textLabelMaxWidth) {
        self.textLabel.frame = CGRectSetWidth(self.textLabel.frame, textLabelMaxWidth);
    }
    
    self.detailTextLabel.frame = CGRectSetXY(self.detailTextLabel.frame, CGRectGetMaxX(self.textLabel.frame) + self.albumNameInsets.right, [self.detailTextLabel qmui_topWhenCenterInSuperview]);
}

- (void)setAlbumNameFont:(UIFont *)albumNameFont {
    _albumNameFont = albumNameFont;
    self.textLabel.font = albumNameFont;
}

- (void)setAlbumNameColor:(UIColor *)albumNameColor {
    _albumNameColor = albumNameColor;
    self.textLabel.textColor = albumNameColor;
}

- (void)setAlbumAssetsNumberFont:(UIFont *)albumAssetsNumberFont {
    _albumAssetsNumberFont = albumAssetsNumberFont;
    self.detailTextLabel.font = albumAssetsNumberFont;
}

- (void)setAlbumAssetsNumberColor:(UIColor *)albumAssetsNumberColor {
    _albumAssetsNumberColor = albumAssetsNumberColor;
    self.detailTextLabel.textColor = albumAssetsNumberColor;
}

@end


#pragma mark - QMUIAlbumViewController (UIAppearance)

@implementation QMUIAlbumViewController (UIAppearance)

+ (void)initialize {
    static dispatch_once_t onceToken1;
    dispatch_once(&onceToken1, ^{
        [self appearance]; // +initialize 时就先设置好默认样式
    });
}

static QMUIAlbumViewController *albumViewControllerAppearance;
+ (nonnull instancetype)appearance {
    static dispatch_once_t onceToken2;
    dispatch_once(&onceToken2, ^{
        if (!albumViewControllerAppearance) {
            albumViewControllerAppearance = [[QMUIAlbumViewController alloc] init];
            albumViewControllerAppearance.albumTableViewCellHeight = 88;
        }
    });
    return albumViewControllerAppearance;
}

@end


#pragma mark - QMUIAlbumViewController

@interface QMUIAlbumViewController ()

@property(nonatomic, strong) NSMutableArray<QMUIAssetsGroup *> *albumsArray;
@property(nonatomic, strong) QMUIImagePickerViewController *imagePickerViewController;
@end

@implementation QMUIAlbumViewController

- (void)didInitialize {
    [super didInitialize];
    _shouldShowDefaultLoadingView = YES;
    if (albumViewControllerAppearance) {
        // 避免 albumViewControllerAppearance init 时走到这里来，导致死循环
        self.albumTableViewCellHeight = [QMUIAlbumViewController appearance].albumTableViewCellHeight;
    }
}

- (void)setupNavigationItems {
    [super setupNavigationItems];
    if (!self.title) {
        self.title = @"照片";
    }
    self.navigationItem.rightBarButtonItem = [UIBarButtonItem qmui_itemWithTitle:@"取消" target:self action:@selector(handleCancelSelectAlbum:)];
}

- (void)initTableView {
    [super initTableView];
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    if ([QMUIAssetsManager authorizationStatus] == QMUIAssetAuthorizationStatusNotAuthorized) {
        // 如果没有获取访问授权，或者访问授权状态已经被明确禁止，则显示提示语，引导用户开启授权
        NSString *tipString = self.tipTextWhenNoPhotosAuthorization;
        if (!tipString) {
            NSDictionary *mainInfoDictionary = [[NSBundle mainBundle] infoDictionary];
            NSString *appName = [mainInfoDictionary objectForKey:@"CFBundleDisplayName"];
            if (!appName) {
                appName = [mainInfoDictionary objectForKey:(NSString *)kCFBundleNameKey];
            }
            tipString = [NSString stringWithFormat:@"请在设备的\"设置-隐私-照片\"选项中，允许%@访问你的手机相册", appName];
        }
        [self showEmptyViewWithText:tipString detailText:nil buttonTitle:nil buttonAction:nil];
    } else {
        self.albumsArray = [[NSMutableArray alloc] init];
        // 获取相册列表较为耗时，交给子线程去处理，因此这里需要显示 Loading
        if ([self.albumViewControllerDelegate respondsToSelector:@selector(albumViewControllerWillStartLoading:)]) {
            [self.albumViewControllerDelegate albumViewControllerWillStartLoading:self];
        }
        if (self.shouldShowDefaultLoadingView) {
            [self showEmptyViewWithLoading];
        }
        dispatch_async(dispatch_get_global_queue(0, 0), ^{
            __weak __typeof(self)weakSelf = self;
            [[QMUIAssetsManager sharedInstance] enumerateAllAlbumsWithAlbumContentType:self.contentType usingBlock:^(QMUIAssetsGroup *resultAssetsGroup) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    // 这里需要对 UI 进行操作，因此放回主线程处理
                    __strong __typeof(weakSelf)strongSelf = weakSelf;
                    if (resultAssetsGroup) {
                        [strongSelf.albumsArray addObject:resultAssetsGroup];
                    } else {
                        [strongSelf refreshAlbumAndShowEmptyTipIfNeed];
                    }
                });
            }];
        });
    }
}

- (void)refreshAlbumAndShowEmptyTipIfNeed {
    if ([self.albumsArray count] > 0) {
        if ([self.albumViewControllerDelegate respondsToSelector:@selector(albumViewControllerWillFinishLoading:)]) {
            [self.albumViewControllerDelegate albumViewControllerWillFinishLoading:self];
        }
        if (self.shouldShowDefaultLoadingView) {
            [self hideEmptyView];
        }
        [self.tableView reloadData];
    } else {
        NSString *tipString = self.tipTextWhenPhotosEmpty ? : @"空照片";
        [self showEmptyViewWithText:tipString detailText:nil buttonTitle:nil buttonAction:nil];
    }
}

- (void)pickAlbumsGroup:(QMUIAssetsGroup *)assetsGroup animated:(BOOL)animated {
    if (!assetsGroup) return;
    
    if (!self.imagePickerViewController) {
        self.imagePickerViewController = [self.albumViewControllerDelegate imagePickerViewControllerForAlbumViewController:self];
    }
    NSAssert(self.imagePickerViewController, @"self.%@ 必须实现 %@ 并返回一个 %@ 对象", NSStringFromSelector(@selector(albumViewControllerDelegate)), NSStringFromSelector(@selector(imagePickerViewControllerForAlbumViewController:)), NSStringFromClass([QMUIImagePickerViewController class]));
    
    [self.imagePickerViewController refreshWithAssetsGroup:assetsGroup];
    self.imagePickerViewController.title = [assetsGroup name];
    [self.navigationController pushViewController:self.imagePickerViewController animated:animated];
}

- (void)pickLastAlbumGroupDirectlyIfCan {
    QMUIAssetsGroup *assetsGroup = [QMUIImagePickerHelper assetsGroupOfLastPickerAlbumWithUserIdentify:nil];
    [self pickAlbumsGroup:assetsGroup animated:NO];
}

#pragma mark - <UITableViewDelegate,UITableViewDataSource>

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.albumsArray count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return self.albumTableViewCellHeight;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *kCellIdentifer = @"cell";
    QMUIAlbumTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kCellIdentifer];
    if (!cell) {
        cell = [[QMUIAlbumTableViewCell alloc] initForTableView:tableView withStyle:UITableViewCellStyleSubtitle reuseIdentifier:kCellIdentifer];
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    }
    QMUIAssetsGroup *assetsGroup = [self.albumsArray objectAtIndex:indexPath.row];
    // 显示相册缩略图
    cell.imageView.image = [assetsGroup posterImageWithSize:CGSizeMake(self.albumTableViewCellHeight, self.albumTableViewCellHeight)];
    // 显示相册名称
    cell.textLabel.text = [assetsGroup name];
    // 显示相册中所包含的资源数量
    cell.detailTextLabel.text = [NSString stringWithFormat:@"· %@", @(assetsGroup.numberOfAssets)];
    [cell updateCellAppearanceWithIndexPath:indexPath];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self pickAlbumsGroup:self.albumsArray[indexPath.row] animated:YES];
}

- (void)handleCancelSelectAlbum:(id)sender {
    [self dismissViewControllerAnimated:YES completion:^(void) {
        if (self.albumViewControllerDelegate && [self.albumViewControllerDelegate respondsToSelector:@selector(albumViewControllerDidCancel:)]) {
            [self.albumViewControllerDelegate albumViewControllerDidCancel:self]; 
        }
        [self.imagePickerViewController.selectedImageAssetArray removeAllObjects];
    }];
}

@end
