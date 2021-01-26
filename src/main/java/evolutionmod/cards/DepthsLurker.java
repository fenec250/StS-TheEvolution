package evolutionmod.cards;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Darkness;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.DepthsLurkerAction;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.ShadowsPower;

import java.util.List;

public class DepthsLurker
        extends BaseEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:DepthsLurker";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ShadowSkl.png";
    private static final int COST = 1;
    private static final int FORM_BLOCK_AMT = 4;
    private static final int UPGRADE_FORM_BLOCK_AMT = 2;
    private static final int FORM_SHADOWS_AMT = 2;
    private static final int UPGRADE_FORM_SHADOWS_AMT = 1;

    public DepthsLurker() {
        super(ID, NAME, new RegionName("blue/skill/darkness"), COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.block = this.baseBlock = FORM_BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = FORM_SHADOWS_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isPlayerInThisForm(ShadowGene.ID)) {
            addToBot(new ApplyPowerAction(p, p, new ShadowsPower(p, this.magicNumber)));
        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
//                ShadowsPower.reduceThreshold(p, magicNumber);
                ShadowsPower.triggerImmediately(p);
                this.isDone = true;
            }
        });
        BaseEvoCard.formEffect(ShadowGene.ID);
        BaseEvoCard.formEffect(MerfolkGene.ID, () -> addToBot(new GainBlockAction(p, this.block)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DepthsLurker();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_FORM_BLOCK_AMT);
            this.upgradeMagicNumber(UPGRADE_FORM_SHADOWS_AMT);
        }
    }

    @Override
    public int getNumberOfGlows() {
        return 2;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return true;
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        switch (glowIndex) {
            case 0:
                return isPlayerInThisForm(ShadowGene.ID) ? ShadowGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
            case 1:
                return isPlayerInThisForm(MerfolkGene.ID, ShadowGene.ID) ? MerfolkGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
            default:
                return BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (customTooltips == null) {
            super.getCustomTooltips();
            customTooltips.add(new TooltipInfo(BlackCat.EXTENDED_DESCRIPTION[0],
                    BlackCat.EXTENDED_DESCRIPTION[1]));
        }
        return  customTooltips;
    }
}