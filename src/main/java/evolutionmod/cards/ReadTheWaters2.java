package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.LymeanGene2;
import evolutionmod.patches.EvolutionEnum;

public class ReadTheWaters2
        extends BaseEvoCard implements OnShuffleCard {
    public static final String ID = "evolutionmodV2:ReadTheWaters";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CalmTheWaters.png";
    private static final int COST = 1;
//    private static final int LYMEAN_FATE_AMT = 2;
//    private static final int UPGRADE_LYMEAN_FATE_AMT = 1;
    private static final int BLOCK_AMT = 8;
    private static final int UPGRADE_BLOCK_AMT = 3;

    public ReadTheWaters2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.NONE);
//        this.magicNumber = this.baseMagicNumber = LYMEAN_FATE_AMT;
        this.block = this.baseBlock = BLOCK_AMT;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        int fate = this.magicNumber;
//        int block = this.block;
//        if (this.upgraded) {
//            addToBot(new ChannelAction(new MerfolkGene()));
//        } else {
//            BaseEvoCard.formEffect(MerfolkGene.ID);
//        }
//        boolean inForm = BaseEvoCard.formEffect(LymeanGene.ID);
//        if (inForm) {
//            fate += LYMEAN_FATE_AMT;
//        }
//        final int finalFate = fate;
        addToBot(new GainBlockAction(p, block));
//        HashMap<Predicate<AbstractCard>, Integer> selectors = new HashMap<Predicate<AbstractCard>, Integer>() {{
//            put(c -> c.type == CardType.SKILL, finalFate);
//        }};
//        addToBot(new FateAction(selectors));
//        addToBot(new CalmTheWatersAction(fate));
//        if (formEffect(LymeanGene.ID)) {
//            addToBot(new ApplyPowerAction(p, p, new FatePower(p, this.magicNumber)));
//        addToBot(new MerfolkGene().getChannelAction());
        this.exhaust = false;
        formEffect(LymeanGene2.ID, () -> this.exhaust = true);
//        }
    }

    @Override
    public void onShuffleFromExhaustPile() {
        this.isEthereal = true;
        initializeDescription();
        addToBot(new ExhaustToHandAction(this));
    }
    //    @Override
//    protected void applyPowersToBlock() {
//        alterBlockAround(() -> super.applyPowersToBlock());
//    }
//
//    private void alterBlockAround(Runnable supercall) {
//        this.baseBlock = BLOCK_AMT + (upgraded ? UPGRADE_BLOCK_AMT : 0);
//        if (!this.upgraded && isPlayerInThisForm(MerfolkGene.ID)) {
//            this.baseBlock += UPGRADE_BLOCK_AMT;
//        }
//        supercall.run();
//        this.baseBlock = BLOCK_AMT + (upgraded ? UPGRADE_BLOCK_AMT : 0);
//        this.isBlockModified = this.block != this.baseBlock;
//    }

    @Override
    public AbstractCard makeCopy() {
        return new ReadTheWaters2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
//            this.upgradeMagicNumber(UPGRADE_LYMEAN_FATE_AMT);
            this.initializeDescription();
        }
    }

    @Override
    public void initializeDescription() {
        this.rawDescription = this.isEthereal ? EXTENDED_DESCRIPTION[0] : DESCRIPTION;
        super.initializeDescription();
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(LymeanGene2.ID)) {
            this.glowColor = LymeanGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}