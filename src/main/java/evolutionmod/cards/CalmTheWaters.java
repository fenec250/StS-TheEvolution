package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.CalmTheWatersAction;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.patches.AbstractCardEnum;

public class CalmTheWaters
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:CalmTheWaters";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LymeanSkl.png";
    private static final int COST = 1;
    private static final int FATE_AMT = 2;
    private static final int LYMEAN_FATE_AMT = 1;
    private static final int BLOCK_AMT = 3;
    private static final int UPGRADE_BLOCK_AMT = 3;

    public CalmTheWaters() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION + UPGRADE_BLOCK_AMT + EXTENDED_DESCRIPTION[0],
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = FATE_AMT;
        this.block = this.baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int fate = this.magicNumber;
        int block = this.block;
        if (this.upgraded) {
            addToBot(new ChannelAction(new MerfolkGene()));
        } else {
            BaseEvoCard.formEffect(MerfolkGene.ID);
        }
        boolean inForm = BaseEvoCard.formEffect(LymeanGene.ID);
        if (inForm) {
            fate += LYMEAN_FATE_AMT;
        }
        addToTop(new GainBlockAction(p, block));
        addToBot(new CalmTheWatersAction(fate));
    }

    @Override
    protected void applyPowersToBlock() {
        alterBlockAround(() -> super.applyPowersToBlock());
    }

    private void alterBlockAround(Runnable supercall) {
        this.baseBlock = BLOCK_AMT + (upgraded ? UPGRADE_BLOCK_AMT : 0);
        if (!this.upgraded && isPlayerInThisForm(MerfolkGene.ID)) {
            this.baseBlock += UPGRADE_BLOCK_AMT;
        }
        supercall.run();
        this.baseBlock = BLOCK_AMT + (upgraded ? UPGRADE_BLOCK_AMT : 0);
        this.isBlockModified = this.block != this.baseBlock;
    }

    @Override
    public AbstractCard makeCopy() {
        return new CalmTheWaters();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(LymeanGene.ID) && (upgraded || isPlayerInThisForm(MerfolkGene.ID))) {
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}