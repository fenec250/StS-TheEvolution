package evolutionmod.cardsV1;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.ShadowsPower;

import java.util.List;
import evolutionmod.orbsV1.*;

public class DepthsLurker
        extends BaseEvoCard {
    public static final String cardID = "DepthsLurker";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/DepthsLurker.png";
    private static final int COST = 1;
//    private static final int FORM_BLOCK_AMT = 4;
//    private static final int UPGRADE_FORM_BLOCK_AMT = 2;
    private static final int FORM_SHADOWS_AMT = 2;
    private static final int UPGRADE_FORM_SHADOWS_AMT = 1;

    public DepthsLurker() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
//        this.block = this.baseBlock = FORM_BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = FORM_SHADOWS_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        if (isPlayerInThisForm(ShadowGene.ID)) {
//            addToBot(new ApplyPowerAction(p, p, new ShadowsPower(p, this.magicNumber)));
//        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
//                ShadowsPower.reduceThreshold(p, magicNumber);
                ShadowsPower.triggerImmediately(p);
                this.isDone = true;
            }
        });
//        BaseEvoCard.formEffect(ShadowGene.ID);
        BaseEvoCard.formEffect(MerfolkGene.ID, () -> addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                addToTop(new GainBlockAction(p, ShadowsPower.getThreshold(p) * magicNumber));
                this.isDone = true;
            }
        }));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DepthsLurker();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeBlock(UPGRADE_FORM_BLOCK_AMT);
            this.upgradeMagicNumber(UPGRADE_FORM_SHADOWS_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        this.glowColor = isPlayerInThisForm(MerfolkGene.ID, ShadowGene.ID) ? MerfolkGene.COLOR.cpy()
                : BLUE_BORDER_GLOW_COLOR.cpy();
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