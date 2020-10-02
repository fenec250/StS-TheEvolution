package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.ShadowShiftAction;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.patches.AbstractCardEnum;

public class ShadowShift
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:ShadowShift";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/GhostForm.png";
    private static final int COST = 2;
    private static final int BLOCK_AMT = 4;
    private static final int UPGRADE_BLOCK_AMT = 2;

    public ShadowShift() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
//        this.magicNumber = this.baseMagicNumber = THORN_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ShadowShiftAction(p, this.block));
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new ShadowGene()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShadowShift();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
//            this.upgradeMagicNumber(UPGRADE_THORN_AMT);
        }
    }
}